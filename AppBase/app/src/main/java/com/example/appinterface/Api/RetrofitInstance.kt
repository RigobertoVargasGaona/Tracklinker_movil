package com.example.appinterface.Api

import android.content.Context
import com.example.appinterface.Api.Services.*
import com.example.appinterface.helpers.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL_APIKOTLIN = "http://192.168.2.68:8080"
    private const val BASE_URL_PYTHON_API = "http://192.168.2.68:8000/api/"

    private var tokenManager: TokenManager? = null

    fun init(context: Context) {
        if (tokenManager == null) {
            tokenManager = TokenManager(context.applicationContext)
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val token = tokenManager?.getAccessToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        val response = chain.proceed(request)
        if (response.code == 401) {
            tokenManager?.clearTokens()
        }
        response
    }

    private val publicClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val privateClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun kotlinRetrofit(auth: Boolean = true) = Retrofit.Builder()
        .baseUrl(BASE_URL_APIKOTLIN)
        .client(if (auth) privateClient else publicClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun pythonRetrofit(auth: Boolean = true) = Retrofit.Builder()
        .baseUrl(BASE_URL_PYTHON_API)
        .client(if (auth) privateClient else publicClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Sin token → solo login
    val authApi: AuthApiService by lazy {
        kotlinRetrofit(auth = false).create(AuthApiService::class.java)
    }

    // Con token → resto de servicios
    val api2kotlin: WarrantiesApiService by lazy {
        kotlinRetrofit().create(WarrantiesApiService::class.java)
    }

    val apiProducts: ProductService by lazy {
        kotlinRetrofit().create(ProductService::class.java)
    }

    val usersApi: UsersApiService by lazy {
        kotlinRetrofit().create(UsersApiService::class.java)
    }

    val categoryApi: CategoriesApiService by lazy {
        kotlinRetrofit().create(CategoriesApiService::class.java)
    }

    val outputOrderApi: OutputOrderService by lazy {
        pythonRetrofit().create(OutputOrderService::class.java)
    }

    val reportsApi: HomeService by lazy {
        pythonRetrofit().create(HomeService::class.java)
    }
}