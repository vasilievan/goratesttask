package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R.id.users_recyclerview
import aleskey.vasiliev.goratesttask.databinding.UsersDefaultViewBinding
import aleskey.vasiliev.goratesttask.model.NetworkInstance.getAlbums
import aleskey.vasiliev.goratesttask.model.NetworkInstance.getUsers
import aleskey.vasiliev.goratesttask.model.SharedData.ALBUMS
import aleskey.vasiliev.goratesttask.model.SharedData.APPLICATION_CONTEXT
import aleskey.vasiliev.goratesttask.model.SharedData.PHONE_WIDTH
import aleskey.vasiliev.goratesttask.model.SharedData.USERS
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.thread

/**
 * Тестовое приложение для GORA Studio. Представляет собой галерею для заданных пользователей
 * с сайта <a href="https://jsonplaceholder.typicode.com/"></a>
 * @author <a href="mailto:enthusiastic.programmer@yandex.ru">Алексей Васильев</a>
 * @version 1.0
 *
 * Данный класс - активность, на которой выводятся доступные пользователи.
 */

class Users : AppCompatActivity() {

    private lateinit var binding: UsersDefaultViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsersDefaultViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        APPLICATION_CONTEXT = applicationContext
        setPhoneWidth()
        val recyclerView = view.getViewById(users_recyclerview) as RecyclerView
        thread {
            USERS = getUsers()
            ALBUMS = getAlbums(USERS)
            runOnUiThread {
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = UsersRecyclerViewAdapter(USERS)
                }
            }
        }
    }

    private fun setPhoneWidth() {
        val display = windowManager.defaultDisplay
        val realMetrics = DisplayMetrics()
        display.getRealMetrics(realMetrics)
        PHONE_WIDTH = realMetrics.widthPixels
    }
}