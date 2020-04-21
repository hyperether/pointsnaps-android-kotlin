package com.hyperether.pointsnaps.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.pointsnapssdk.PointSnapsSDK
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {
    var descriptionData = MutableLiveData<String>()
    var error = MutableLiveData<String>()
    var successUpload = MutableLiveData<Boolean>()

    fun setDescription(description: String){
        descriptionData.postValue(description)
    }

    fun getDescriptionLiveData(): MutableLiveData<String> {
        return descriptionData
    }

    fun upload(file: File, fileName: String, ext: String, address:String, lon: Double, lat: Double, description: String) {
        viewModelScope.launch {
            val response = PointSnapsSDK.uploadImage(file, fileName, ext, address, lon, lat, description)
            if (response.success) {
                successUpload.postValue(response.success)
            } else {
                error.postValue(response.message)
            }
        }
    }
}
