package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.DataResponse
import retrofit2.Call
import retrofit2.http.GET
interface ApiService {
    @GET("breed/hound/images")
    fun getHoundImages(): Call<DataResponse>


}