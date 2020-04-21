package com.hyperether.pointsnaps.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var descriptionData = MutableLiveData<String>()

    fun setDescription(description: String){
        descriptionData.postValue(description)
    }

    fun getDescriptionLiveData(): MutableLiveData<String> {
        return descriptionData
    }
}
