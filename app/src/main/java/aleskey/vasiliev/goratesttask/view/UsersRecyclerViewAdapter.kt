package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R.id.username_textview
import aleskey.vasiliev.goratesttask.R.layout.username_view
import aleskey.vasiliev.goratesttask.model.NetworkInstance
import aleskey.vasiliev.goratesttask.model.SharedData.openPhotos
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersRecyclerViewAdapter(private val dataSet: List<NetworkInstance.User>) :
    RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(username_textview)
        init {
            textView.setOnClickListener {
                openPhotos(it as TextView)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(username_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size


}