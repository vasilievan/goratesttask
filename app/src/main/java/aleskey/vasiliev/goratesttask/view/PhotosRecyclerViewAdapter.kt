package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R
import aleskey.vasiliev.goratesttask.R.id.photo
import aleskey.vasiliev.goratesttask.R.id.photo_title
import aleskey.vasiliev.goratesttask.R.layout.photo_pattern
import aleskey.vasiliev.goratesttask.model.NetworkInstance
import aleskey.vasiliev.goratesttask.model.SharedData.IMAGE_MARGIN
import aleskey.vasiliev.goratesttask.model.SharedData.PHONE_WIDTH
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/** Данные на активити формируются динамически с использованием RecyclerView. Элементы данного -
 * картинка и её подпись.
 */

class PhotosRecyclerViewAdapter(private val data: ArrayList<NetworkInstance.PhotoInstance>) :
    RecyclerView.Adapter<PhotosRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(photo_title)
        val imageView: ImageView = view.findViewById(photo)
        val pb: ProgressBar = view.findViewById(R.id.progress_bar)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(photo_pattern, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (data[position].title != null) {
            viewHolder.pb.visibility = View.INVISIBLE
            viewHolder.imageView.layoutParams.width = PHONE_WIDTH - IMAGE_MARGIN
            viewHolder.imageView.layoutParams.height = PHONE_WIDTH - IMAGE_MARGIN
        }
        viewHolder.textView.text = data[position].title
        viewHolder.imageView.setImageBitmap(data[position].bm)
    }

    override fun getItemCount() = data.size
}