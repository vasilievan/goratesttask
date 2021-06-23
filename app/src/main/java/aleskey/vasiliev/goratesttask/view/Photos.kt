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
import aleskey.vasiliev.goratesttask.model.NetworkInstance.getPhotosById

class Photos : AppCompatActivity() {

    private lateinit var binding: PhotosViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PhotosViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val username = intent.getStringExtra(USERNAME)
        val id = USERS.firstOrNull { it.name == username }?.id
        val photoslist = getPhotosById(id!!)
        val myData = arrayListOf<NetworkInstance.PhotoInstance>()
        photoslist.forEach { _ -> myData.add(NetworkInstance.PhotoInstance(null, null)) }
        val recyclerView = view.getViewById(R.id.photos_recyclerview) as RecyclerView
        val myAdapter = PhotosRecyclerViewAdapter(myData)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = myAdapter
            setHasFixedSize(true)
        }
        thread {
            for (index in photoslist.indices) {
                val bm = loadPhotoByURL(photoslist[index].url_string)
                myData[index] = NetworkInstance.PhotoInstance(photoslist[index].title, bm)
                runOnUiThread {
                    myAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}