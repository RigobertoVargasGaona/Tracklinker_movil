package com.example.appinterface.Api

import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.Api.Services.CategoriesApiService
import com.example.appinterface.Api.Services.OutputOrderService
import com.example.appinterface.Api.Services.UsersApiService
import com.example.appinterface.Api.Services.WarrantiesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.appinterface.Api.Services.ProductService
object RetrofitInstance {

    private const val BASE_URL_APIKOTLIN = "http://192.168.2.68:8080"
    private const val BASE_URL_PYTHON_API = "http://192.168.2.68:8000/api/"

  
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

    val usersApi: UsersApiService by lazy {
        Retrofit.Builder()
        .baseUrl(BASE_URL_APIKOTLIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UsersApiService::class.java)
    }

    val categoryApi : CategoriesApiService by lazy {
        Retrofit.Builder()
        .baseUrl(BASE_URL_APIKOTLIN)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CategoriesApiService::class.java)
    }

    val outputOrderApi: OutputOrderService by lazy {
        Retrofit.Builder()
        .baseUrl(BASE_URL_PYTHON_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OutputOrderService::class.java)
    }
}


