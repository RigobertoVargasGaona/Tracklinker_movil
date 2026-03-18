package com.example.appinterface.Api

import com.example.appinterface.Api.Services.UsersApiService
import com.example.appinterface.Api.Services.WarrantiesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitInstance {

    private const val BASE_URL_KOTLIN_API = "http://10.0.2.2:8080"
    private const val BASE_URL_PYTHON_API = "http://10.0.2.2:8000"

    val api2kotlin: WarrantiesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_KOTLIN_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WarrantiesApiService::class.java)
    }

    val usersApi: UsersApiService by lazy {
        Retrofit.Builder()
        .baseUrl(BASE_URL_KOTLIN_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UsersApiService::class.java)
    }

}