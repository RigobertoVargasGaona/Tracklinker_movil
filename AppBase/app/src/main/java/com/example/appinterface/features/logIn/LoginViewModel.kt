package com.example.appinterface.features.logIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val loginState: LiveData<LoginUiState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginUiState.Error("Completa todos los campos")
            return
        }
        _loginState.value = LoginUiState.Loading
        viewModelScope.launch {
            repository.login(email, password)
                .onSuccess { _loginState.value = LoginUiState.Success }
                .onFailure { _loginState.value = LoginUiState.Error(it.message ?: "Error desconocido") }
        }
    }
}