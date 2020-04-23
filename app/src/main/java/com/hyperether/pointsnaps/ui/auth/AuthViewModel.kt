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
    val registerUser = MutableLiveData<User>()


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

    fun registerUser(email: String, firstName: String, lastName: String, password: String, code: String) {
        viewModelScope.launch {
            val result = PointSnapsSDK.register(email, firstName, lastName, password, code)
            if (result.success) {
                registerUser.postValue(User(firstName, lastName, password, email, false, false, ""))
            } else {
                error.postValue(result.message)
            }
        }
    }
}
