package aleskey.vasiliev.goratesttask.model

import aleskey.vasiliev.goratesttask.R.string.network_issue
import aleskey.vasiliev.goratesttask.model.SharedData.ALBUMS
import aleskey.vasiliev.goratesttask.model.SharedData.ALBUM_ID
import aleskey.vasiliev.goratesttask.model.SharedData.APPLICATION_CONTEXT
import aleskey.vasiliev.goratesttask.model.SharedData.ID
import aleskey.vasiliev.goratesttask.model.SharedData.NAME
import aleskey.vasiliev.goratesttask.model.SharedData.PHOTO_URL
import aleskey.vasiliev.goratesttask.model.SharedData.TITLE
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.widget.Toast
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object NetworkInstance {

    data class User(val id: Int, val name: String)

    data class Photo(val title: String, val url: URL)

    private const val DATA_SERVER = "https://jsonplaceholder.typicode.com"
    private const val USERS_URL_STRING = "$DATA_SERVER/users"
    private const val PHOTOS_URL_STRING = "$DATA_SERVER/photos"
    private const val ALBUMS_URL_STRING = "$DATA_SERVER/albums"

    private fun isConnectionAvailable(): Boolean {
        val cm = APPLICATION_CONTEXT.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

    private fun readLines(httpURLConnection: HttpURLConnection): String {
        val responseContent = StringBuilder()
        httpURLConnection.inputStream.bufferedReader().use {
            it.lines().forEach { line -> responseContent.append(line) }
        }
        return responseContent.toString()
    }

    private fun parseUsers(response: String): List<User> {
        val jsonArray = JSONArray(response)
        var index = 0
        val users = mutableListOf<User>()
        while (jsonArray.length() != index) {
            val currentUserString = jsonArray.get(index).toString()
            val currentUserObj = JSONObject(currentUserString)
            val id = currentUserObj.getInt(ID)
            val name = currentUserObj.getString(NAME)
            users.add(User(id, name))
            index++
        }
        return users
    }

    private fun parseAlbums(response: String): Map<Int, MutableSet<Int>> {
        val jsonArray = JSONArray(response)
        var index = 0
        val albums = mutableMapOf<Int, MutableSet<Int>>()
        while (jsonArray.length() != index) {
            val currentUserString = jsonArray.get(index).toString()
            val currentUserObj = JSONObject(currentUserString)
            val userID = currentUserObj.getInt("userId")
            val albumID = currentUserObj.getInt("id")
            if (albums[userID].isNullOrEmpty()) {
                albums[userID] = mutableSetOf(albumID)
            } else {
                albums[userID]!!.add(albumID)
            }
            index++
        }
        return albums
    }

    private fun parsePhotosByID(response: String, ID: Int): List<Photo> {
        val jsonArray = JSONArray(response)
        var index = 0
        val photos = mutableListOf<Photo>()
        while (jsonArray.length() != index) {
            val currentUserString = jsonArray.get(index).toString()
            val currentUserObj = JSONObject(currentUserString)
            val albumID = currentUserObj.getInt(ALBUM_ID)
            if (albumID in ALBUMS[ID]!!) {
                val title = currentUserObj.getString(TITLE)
                val url = currentUserObj.getString(PHOTO_URL)
                photos.add(Photo(title, URL(url)))
            }
            index++
        }
        return photos
    }

    private suspend fun getDeferredUserData(): Deferred<List<User>> = withContext(Dispatchers.Default) {
        async {
            val usernames: MutableList<User> = mutableListOf()
            val usersUrl = URL(USERS_URL_STRING)
            if (isConnectionAvailable()) {
                with(usersUrl.openConnection() as HttpURLConnection) {
                    val response = readLines(this)
                    usernames.addAll(parseUsers(response))
                }
            } else {
                Toast.makeText(APPLICATION_CONTEXT, network_issue, Toast.LENGTH_LONG).show()
            }
            usernames
        }
    }

    private suspend fun getDeferredAlbumsData(users: List<User>): Deferred<Map<Int, Set<Int>>> =
        withContext(Dispatchers.Default) {
            var albums: Map<Int, Set<Int>>? = null
            async {
            val albumsUrl = URL(ALBUMS_URL_STRING)
            if (isConnectionAvailable()) {
                with(albumsUrl.openConnection() as HttpURLConnection) {
                    val response = readLines(this)
                    albums = parseAlbums(response)
                }
            } else {
                Toast.makeText(APPLICATION_CONTEXT, network_issue, Toast.LENGTH_LONG).show()
            }
            albums!!
        }
    }

    private suspend fun getDeferredPhotosDataByID(ID: Int): Deferred<List<Photo>> =
        withContext(Dispatchers.Default) {
            var photos: List<Photo>? = null
            async {
                val albumsUrl = URL(PHOTOS_URL_STRING)
                if (isConnectionAvailable()) {
                    with(albumsUrl.openConnection() as HttpURLConnection) {
                        val response = readLines(this)
                        photos = parsePhotosByID(response, ID)
                    }
                } else {
                    Toast.makeText(APPLICATION_CONTEXT, network_issue, Toast.LENGTH_LONG).show()
                }
                photos!!
            }
        }

    fun getPhotosById(id: Int): List<Photo> = runBlocking {
        getDeferredPhotosDataByID(id).await()
    }

    fun getUsers(): List<User> = runBlocking {
        getDeferredUserData().await()
    }

    fun getAlbums(users: List<User>): Map<Int, Set<Int>> = runBlocking {
        getDeferredAlbumsData(users).await()
    }
}