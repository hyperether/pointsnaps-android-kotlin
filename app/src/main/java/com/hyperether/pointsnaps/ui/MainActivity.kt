package com.hyperether.pointsnaps.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.hyperether.pointsnaps.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //todo check if user is logged in
        findNavController(R.id.nav_host_fragment).navigate(R.id.goToMainNav)
    }

}
