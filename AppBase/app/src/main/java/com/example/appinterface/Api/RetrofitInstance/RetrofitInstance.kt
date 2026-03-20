package com.example.appinterface.Api

import com.example.appinterface.Api.Services.ApiServicesWarranties
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.appinterface.Api.Services.ProductService
object RetrofitInstance {

    private const val BASE_URL_APIKOTLIN = "http://10.0.2.2:8080"



    val api2kotlin: ApiServicesWarranties by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicesWarranties::class.java)

    }
    val apiProducts: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}


