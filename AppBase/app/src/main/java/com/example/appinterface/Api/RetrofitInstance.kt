package com.example.appinterface.Api.RetrofitInstance

import com.example.appinterface.Api.Services.WarrantiesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.appinterface.Api.Services.ProductService
object RetrofitInstance {

    private const val BASE_URL_APIKOTLIN = "http://10.0.2.2:8080"

  
    val api2kotlin: WarrantiesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WarrantiesApiService::class.java)
    }

    val apiProducts: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}


