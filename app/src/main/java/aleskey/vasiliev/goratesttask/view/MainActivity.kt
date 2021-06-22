package aleskey.vasiliev.goratesttask.view

import aleskey.vasiliev.goratesttask.R
import aleskey.vasiliev.goratesttask.model.NetworkInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import aleskey.vasiliev.goratesttask.model.NetworkInstance.APPLICATION_CONTEXT

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APPLICATION_CONTEXT = applicationContext

        val users = Users()
        val photos = Photos()

        NetworkInstance.getUsernames()

        /*supportFragmentManager.beginTransaction().apply {
            replace(..., users)
            addToBackStack(null)
            commit()
        }*/

    }
}