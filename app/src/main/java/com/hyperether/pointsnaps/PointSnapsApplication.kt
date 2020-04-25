package com.hyperether.pointsnaps

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.hyperether.pointsnapssdk.PointSnapsSDK
import com.hyperether.pointsnapssdk.utils.Constants

class PointSnapsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PointSnapsSDK.build {
            context = applicationContext
        }
        val mode = PointSnapsSDK.getMode()
        when (mode) {
            Constants.MODE_DARK ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Constants.MODE_LIGHT ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}