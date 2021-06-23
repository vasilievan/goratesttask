package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.databinding.PhotosBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Photos : AppCompatActivity() {

    private lateinit var binding: PhotosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PhotosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}