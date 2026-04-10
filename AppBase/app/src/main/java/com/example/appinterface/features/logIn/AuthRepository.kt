package com.example.appinterface.features.logIn

import com.example.appinterface.Api.Models.LoginRequest
import com.example.appinterface.Api.Models.TokenResponse
import com.example.appinterface.Api.Services.AuthApiService
import com.example.appinterface.helpers.TokenManager

class AuthRepository(
    private val api: AuthApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<TokenResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.accessToken, body.refreshToken)
                Result.success(body)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = tokenManager.clearTokens()

    fun isLoggedIn() = tokenManager.isLoggedIn()
}