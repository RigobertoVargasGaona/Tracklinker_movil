package com.example.appinterface.features.logIn

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appinterface.helpers.TokenManager

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(TokenManager(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}