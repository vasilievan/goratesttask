package aleskey.vasiliev.goratesttask.model

import aleskey.vasiliev.goratesttask.view.Photos
import android.content.Context
import android.content.Intent
import android.widget.TextView

/** Синглтон, в котором содержатся данные, используемые другими классами.*/

object SharedData {
    const val NAME = "name"
    const val ID = "id"
    const val ALBUM_ID = "albumId"
    const val TITLE = "title"
    const val PHOTO_URL = "url"
    const val USERNAME = "username"
    const val IMAGE_MARGIN = 40
    lateinit var ALBUMS: Map<Int, Set<Int>>
    lateinit var USERS: List<NetworkInstance.User>
    lateinit var APPLICATION_CONTEXT: Context
    var BITMAPS_TO_USERS: MutableMap<Int, ArrayList<NetworkInstance.PhotoInstance>>? = null
    var PHONE_WIDTH = 0


    /** Переход между активностями по нажатию на элемент RecyclerView. */

    fun openPhotos(textView: TextView) {
        val intent = Intent(APPLICATION_CONTEXT, Photos::class.java).apply {
            putExtra(USERNAME, textView.text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        APPLICATION_CONTEXT.startActivity(intent)
    }
}