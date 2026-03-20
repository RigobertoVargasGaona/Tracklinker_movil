package com.example.appinterface.Api.Services


import com.example.appinterface.Modelos.Product
import com.example.appinterface.RESPONSE.GenericResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
 @GET("products")
fun getProducts(): Call<List<Product>>

@POST("products")
fun createProduct(@Body product: Product): Call<GenericResponse>

@PUT("products/{id}")
fun updateProduct(@Path("id") id: Int, @Body product: Product): Call<GenericResponse>

@DELETE("products/{id}")
fun deleteProduct(@Path("id") id: Int): Call<GenericResponse>
}