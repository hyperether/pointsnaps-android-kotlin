package com.hyperether.pointsnaps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //todo check if user is logged in
        findNavController(R.id.nav_host_fragment).navigate(R.id.goToMainNav)
        findNavController(R.id.nav_host_fragment).navigate(R.id.goToAuthNav)
    }

}
