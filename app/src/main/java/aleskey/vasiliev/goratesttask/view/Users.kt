package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R
import aleskey.vasiliev.goratesttask.databinding.UsersDefaultBinding
import aleskey.vasiliev.goratesttask.model.NetworkInstance
import aleskey.vasiliev.goratesttask.model.SharedData.APPLICATION_CONTEXT
import aleskey.vasiliev.goratesttask.model.SharedData.USERS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Users : AppCompatActivity() {

    private lateinit var binding: UsersDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsersDefaultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        APPLICATION_CONTEXT = applicationContext
        val recyclerView = view.getViewById(R.id.users_recyclerview) as RecyclerView
        USERS = NetworkInstance.getUsers()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view.context)
            adapter = UsersRecyclerViewAdapter(USERS)
        }
    }
}