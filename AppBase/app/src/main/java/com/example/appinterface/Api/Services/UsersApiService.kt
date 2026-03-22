package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.CreateUser
import com.example.appinterface.Api.Models.User
import com.example.appinterface.Api.Models.UsersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @GET("users/")
    fun getUsers(): Call<List<User>>

    @POST("users/create")
    fun createUser(@Body user: CreateUser): Call<UsersResponse>

    @PUT("users/update/{id}")
    fun updateUser(@Body user: CreateUser, @Path("id") id: Int): Call<UsersResponse>

    @DELETE("users/delete/{id}")
    fun deleteUser(@Path("id") id: Int) : Call<UsersResponse>
}