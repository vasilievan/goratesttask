package aleskey.vasiliev.goratesttask.model

import aleskey.vasiliev.goratesttask.view.Photos
import android.content.Context
import android.content.Intent
import android.widget.TextView

object SharedData {
    lateinit var USERS: List<NetworkInstance.User>
    lateinit var APPLICATION_CONTEXT: Context

    fun openPhotos(textView: TextView) {
        val intent = Intent(APPLICATION_CONTEXT, Photos::class.java).apply {
            putExtra("username", textView.text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        APPLICATION_CONTEXT.startActivity(intent)
    }
}