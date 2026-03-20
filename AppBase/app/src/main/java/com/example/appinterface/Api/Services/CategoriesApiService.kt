package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.DataResponseCategory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface CategoriesApiService {
    @GET("/categories")
    fun getCategories(): Call<List<DataResponseCategory>>

    @POST("/categories")
    fun createCategory(
        @Body warranty: DataResponseCategory
    ): Call<DataResponseCategory>

    @PUT("/categories/{id}")
    fun updateCategory(
        @Path ("id") id:Int,
        @Body warranty: DataResponseCategory
    ): Call<DataResponseCategory>

    @DELETE("/categories/{id}")
    fun deleteCategory(
        @Path ("id") id:Int
    ): Call<Void>
}



