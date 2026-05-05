package com.example.appinterface.features.logIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appinterface.helpers.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {

    val loginState = MutableLiveData<LoginUiState>(LoginUiState.Idle)

    private val repository = AuthRepository(tokenManager)

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            loginState.value = LoginUiState.Error("Email y contraseña requeridos")
            return
        }

        loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = repository.login(email, password)
            loginState.value = if (result.isSuccess) {
                LoginUiState.Success(result.getOrDefault(""))
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}