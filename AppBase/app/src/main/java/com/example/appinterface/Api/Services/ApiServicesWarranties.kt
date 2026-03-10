package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.DataResponseWarranty
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiServicesWarranties {
    @GET("/warranties")
    fun getWarranties(): Call<List<DataResponseWarranty>>

    @POST("/warranties")
    fun createWarranty(
        @Body warranty: DataResponseWarranty
    ): Call<DataResponseWarranty>

    @PUT("/warranties/{id}")
    fun updateWarranty(
        @Path ("id") id:Int,
        @Body warranty: DataResponseWarranty
    ): Call<DataResponseWarranty>

    @DELETE("/warranties/{id}")
    fun deleteWarranty(
    @Path ("id") id:Int
    ): Call<Void>
}



