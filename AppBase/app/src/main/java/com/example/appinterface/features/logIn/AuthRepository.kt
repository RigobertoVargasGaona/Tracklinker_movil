package com.example.appinterface.features.logIn

import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.Models.LoginRequest
import com.example.appinterface.helpers.TokenManager

class AuthRepository(private val tokenManager: TokenManager) {

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = RetrofitInstance.authApi.login(
                LoginRequest(email, password)
            )
            if (response.isSuccessful) {
                val token = response.body()?.token
                if (token != null) {
                    tokenManager.saveToken(token)
                    Result.success(token)
                } else {
                    Result.failure(Exception("Token vacío"))
                }
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}