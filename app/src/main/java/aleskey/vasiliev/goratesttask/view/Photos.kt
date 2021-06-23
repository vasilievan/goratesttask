package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R
import aleskey.vasiliev.goratesttask.databinding.PhotosViewBinding
import aleskey.vasiliev.goratesttask.model.NetworkInstance
import aleskey.vasiliev.goratesttask.model.NetworkInstance.loadPhotoByURL
import aleskey.vasiliev.goratesttask.model.SharedData.USERNAME
import aleskey.vasiliev.goratesttask.model.SharedData.USERS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.thread

class Photos : AppCompatActivity() {

    private lateinit var binding: PhotosViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PhotosViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val username = intent.getStringExtra(USERNAME)

        val id = USERS.firstOrNull { it.name == username }?.id

        val myData = arrayListOf<NetworkInstance.PhotoInstance>()

        val recyclerView = view.getViewById(R.id.photos_recyclerview) as RecyclerView

        val myAdapter = PhotosRecyclerViewAdapter(myData)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = myAdapter
        }

        val photoslist = NetworkInstance.getPhotosById(id!!)

        thread {
            for (photo in photoslist) {
                val bm = loadPhotoByURL(photo.url_string)
                myData.add(NetworkInstance.PhotoInstance(photo.title, bm))
                runOnUiThread {
                    myAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}