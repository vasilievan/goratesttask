package aleskey.vasiliev.goratesttask.model

import aleskey.vasiliev.goratesttask.R.string.network_issue
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object NetworkInstance {

    data class User(val id: Int, val name: String)

    var APPLICATION_CONTEXT: Context? = null
    private const val DATA_SERVER = "https://jsonplaceholder.typicode.com"
    private const val USERS_URL_STRING = "$DATA_SERVER/users"
    private const val PHOTOS_URL_STRING = "$DATA_SERVER/photos"
    private const val ALBUMS_URL_STRING = "$DATA_SERVER/albums"

    private fun isConnectionAvailable(): Boolean {
        val cm = APPLICATION_CONTEXT!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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
            val id = currentUserObj.getInt("id")
            val name = currentUserObj.getString("name")
            users.add(User(id, name))
            index++
        }
        return users
    }

    fun getUsernames(): List<User>? {
        var usernames: List<User>? = null
        val usersUrl = URL(USERS_URL_STRING)
        if (isConnectionAvailable()) {
            thread {
                GlobalScope.launch(Dispatchers.Main) {
                    val getOperation = async(Dispatchers.IO) {
                        with(usersUrl.openConnection() as HttpURLConnection) {
                            readLines(this)
                        }
                    }
                    val response: String = getOperation.await()
                    usernames = parseUsers(response)
                }
            }
        } else {
            Toast.makeText(APPLICATION_CONTEXT, network_issue, Toast.LENGTH_LONG).show()
        }
        return usernames
    }
}