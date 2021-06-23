package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.databinding.PhotosViewBinding
import aleskey.vasiliev.goratesttask.model.SharedData.USERS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class Photos : AppCompatActivity() {

    private lateinit var binding: PhotosViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PhotosViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val username = intent.getStringExtra("username")
        val id = USERS.firstOrNull { it.name == username }?.id

    }
}