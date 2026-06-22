package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.LoginRequest
import com.example.appinterface.Api.Models.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<TokenResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}