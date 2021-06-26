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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.widget.Toast
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/** Класс для работы с сетью. **/

object NetworkInstance {

    /** Пользователь как сущность JSON. */

    data class User(val id: Int, val name: String)

    /** Фото как сущность JSON. */

    data class Photo(val title: String, val url_string: String)

    /** Фото как изображение с заголовком. Более низкий уровень абстракции, чем предыдущий класс. */

    data class PhotoInstance(val title: String?, val bm: Bitmap?)

    /** Используемые URL. */

    private const val DATA_SERVER = "https://jsonplaceholder.typicode.com"
    private const val USERS_URL_STRING = "$DATA_SERVER/users"
    private const val PHOTOS_URL_STRING = "$DATA_SERVER/photos"
    private const val ALBUMS_URL_STRING = "$DATA_SERVER/albums"
    private const val INTENET_CLIENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
    private const val USER_AGENT = "User-Agent"


    /** Проверка на доступность wifi или мобильного Интернета. */

    private fun isConnectionAvailable(): Boolean {
        val cm = APPLICATION_CONTEXT.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

    /** Построчное чтение JSON с сайта. */

    private fun readLines(httpURLConnection: HttpURLConnection): String {
        val responseContent = StringBuilder()
        httpURLConnection.inputStream.bufferedReader().use {
            it.lines().forEach { line -> responseContent.append(line) }
        }
        return responseContent.toString()
    }

    /** Парсинг пользователя в JSON. */

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

    /** Парсинг принадлежащих пользователю альбомов в JSON. */

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

    /** Парсинг фото по наличию в альбомах конкретного пользователя. */

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
                photos.add(Photo(title, url))
            }
            index++
        }
        return photos
    }

    /** Получение JSON c /users и обработка данных */

    private suspend fun getDeferredUserDataAsync(): Deferred<List<User>> =
        withContext(Dispatchers.Default) {
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

    /** Получение JSON c /albums и обработка данных */

    private suspend fun getDeferredAlbumsDataAsync(users: List<User>): Deferred<Map<Int, Set<Int>>> =
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

    /** Получение JSON c /photos и обработка данных */

    private suspend fun getDeferredPhotosDataByIDAsync(ID: Int): Deferred<List<Photo>> =
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

    /** Получение соответствие изображения ссылке. */

    private suspend fun loadDeferredPhotoByURLAsync(url_string: String): Deferred<Bitmap> =
        withContext(Dispatchers.Default) {
            var myBitmap: Bitmap? = null
            async {
                if (isConnectionAvailable()) {
                    with(URL(url_string).openConnection() as HttpURLConnection) {
                        this.setRequestProperty(
                            USER_AGENT,
                            INTENET_CLIENT
                        )
                        val input = this.inputStream
                        myBitmap = BitmapFactory.decodeStream(input)
                    }
                } else {
                    Toast.makeText(APPLICATION_CONTEXT, network_issue, Toast.LENGTH_LONG).show()
                }
                myBitmap!!
            }
        }

    /** Аналогично предыдущим методам, за исключением отсутствия Deferred */

    fun getPhotosById(id: Int): List<Photo> = runBlocking {
        getDeferredPhotosDataByIDAsync(id).await()
    }

    fun getUsers(): List<User> = runBlocking {
        getDeferredUserDataAsync().await()
    }

    fun getAlbums(users: List<User>): Map<Int, Set<Int>> = runBlocking {
        getDeferredAlbumsDataAsync(users).await()
    }

    fun loadPhotoByURL(url_string: String): Bitmap = runBlocking {
        loadDeferredPhotoByURLAsync(url_string).await()
    }
}