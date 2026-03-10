package com.example.appinterface.Api

import com.example.appinterface.Api.Services.ApiService

import com.example.appinterface.Api.Services.ApiServicesWarranties
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitInstance {
    private const val BASE_URL = "https://dog.ceo/api/"

    private const val BASE_URL_APIKOTLIN = "http://10.0.2.2:8080"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    val api2kotlin: ApiServicesWarranties by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicesWarranties::class.java)
    }

}