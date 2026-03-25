package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.CreateOutputOrder
import com.example.appinterface.Api.Models.DataResponseOutputs
import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.Api.Models.UpdateOutputOrder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OutputOrderService {

    @GET("output_details/")
    fun getOutputs(): Call<DataResponseOutputs>

    @POST("output_details/create")
    fun createOutput(
        @Body outputOrder: CreateOutputOrder
    ): Call<DataResponseOutputs>

    @PUT("output_details/update/{id}")
    fun updateOutput(
        @Path("id") id: Int,
        @Body outputOrder: UpdateOutputOrder
    ): Call<DataResponseOutputs>

    @PUT("output_details/disable/{id}")
    fun disableOutput(
        @Path("id") id: Int
    ): Call<DataResponseOutputs>

    @PUT("output_details/enable/{id}")
    fun enableOutput(
        @Path("id") id: Int
    ): Call<DataResponseOutputs>
}