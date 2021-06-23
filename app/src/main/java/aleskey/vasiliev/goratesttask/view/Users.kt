package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R.id.users_recyclerview
import aleskey.vasiliev.goratesttask.databinding.UsersDefaultViewBinding
import aleskey.vasiliev.goratesttask.model.NetworkInstance.getAlbums
import aleskey.vasiliev.goratesttask.model.NetworkInstance.getUsers
import aleskey.vasiliev.goratesttask.model.SharedData.ALBUMS
import aleskey.vasiliev.goratesttask.model.SharedData.APPLICATION_CONTEXT
import aleskey.vasiliev.goratesttask.model.SharedData.USERS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Users : AppCompatActivity() {

    private lateinit var binding: UsersDefaultViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsersDefaultViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        APPLICATION_CONTEXT = applicationContext
        val recyclerView = view.getViewById(users_recyclerview) as RecyclerView
        USERS = getUsers()
        ALBUMS = getAlbums(USERS)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = UsersRecyclerViewAdapter(USERS)
        }
    }
}