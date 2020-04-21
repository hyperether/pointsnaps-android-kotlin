package com.hyperether.pointsnaps.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.pointsnapssdk.PointSnapsSDK
import com.hyperether.pointsnapssdk.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val user = MutableLiveData<User>()
    val error = MutableLiveData<String>()


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val result = PointSnapsSDK.login(email, password)
            if (result.success) {
                user.postValue(result.data)
            } else {
                error.postValue(result.message)
            }
        }
    }
}
