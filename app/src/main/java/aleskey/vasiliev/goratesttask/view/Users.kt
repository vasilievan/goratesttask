package aleskey.vasiliev.goratesttask.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import aleskey.vasiliev.goratesttask.R.layout.users_fragment

class Users : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(users_fragment, container, false)
    }
}