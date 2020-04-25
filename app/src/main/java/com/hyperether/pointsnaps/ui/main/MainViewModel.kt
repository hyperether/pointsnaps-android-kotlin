package com.hyperether.pointsnaps.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.pointsnaps.model.Location
import com.hyperether.pointsnapssdk.PointSnapsSDK
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {
    var descriptionData = MutableLiveData<String>()
    var error = MutableLiveData<String>()
    var successUpload = MutableLiveData<Boolean>()
    var location = MutableLiveData<Location>()
    var file = MutableLiveData<File>()

    fun setDescription(description: String) {
        descriptionData.postValue(description)
    }

    fun setFile(file: File) {
        this.file.postValue(file)
    }

    fun upload(
        f: File,
        fileName: String,
        ext: String,
        address: String,
        lon: Double,
        lat: Double,
        description: String,
        progressFinish: (Boolean?) -> Unit
    ) {
        viewModelScope.launch {
            val response =
                PointSnapsSDK.uploadImage(f, fileName, ext, address, lon, lat, description)
            if (response.success) {
                file.postValue(File(""))
                descriptionData.postValue("")
                location.postValue(Location())
                successUpload.postValue(response.success)
            } else {
                error.postValue(response.message)
            }
            progressFinish(true)
        }
    }

    fun setLocation(location: Location) {
        this.location.postValue(location)
    }

    suspend fun logout(): Boolean {
        return PointSnapsSDK.logout().success
    }
}
