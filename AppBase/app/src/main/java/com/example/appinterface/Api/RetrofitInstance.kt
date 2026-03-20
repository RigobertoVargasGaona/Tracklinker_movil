package com.example.appinterface.Api.RetrofitInstance

import com.example.appinterface.Api.Services.UsersApiService
import com.example.appinterface.Api.Services.WarrantiesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.appinterface.Api.Services.ProductService
object RetrofitInstance {

    private const val BASE_URL_KOTLIN_API = "http://10.0.2.2:8080"
    private const val BASE_URL_PYTHON_API = "http://10.0.2.2:8000"

  
    val api2kotlin: WarrantiesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_KOTLIN_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicesWarranties::class.java)

    }
    val apiProducts: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_APIKOTLIN)
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
}


