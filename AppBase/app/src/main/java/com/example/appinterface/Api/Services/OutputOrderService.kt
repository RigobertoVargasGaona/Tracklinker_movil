package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.CreateOutputOrder
import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.Api.Models.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OutputOrderService {

    @GET("output_details/")
    fun getOutputs(): Call<List<OutputOrder>>

    @POST("outputs/create")
    fun createOutput(
        @Body outputOrder: CreateOutputOrder
    ): Call<Product>

    @PUT("outputs/update/{id}")
    fun updateOutput(
        @Path("id") id: Int,
        @Body outputOrder: CreateOutputOrder
    ): Call<Product>

    @DELETE("outputs/delete/{id}")
    fun deleteOutput(
        @Path("id") id: Int
    ): Call<Void>
}