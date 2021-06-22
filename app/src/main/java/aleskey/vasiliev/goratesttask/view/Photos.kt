package aleskey.vasiliev.goratesttask.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import aleskey.vasiliev.goratesttask.R.layout.photos_fragment

class Photos : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(photos_fragment, container, false)
    }
}