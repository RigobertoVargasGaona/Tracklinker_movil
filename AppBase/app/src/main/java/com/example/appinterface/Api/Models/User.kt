package com.example.appinterface.Api.Models

data class User(
    val rol_id: Int,
    val rol_name: String? = null,
    val user_id: Int,
    val user_name: String,
    val user_first_surname: String,
    val user_second_surname: String,
    val user_phone: String,
    val user_email: String,
    val user_address: String,
    val user_city: String,
    val user_date: String? = null
)

data class CreateUser(
    val rol_id: Int,
    val user_name: String,
    val user_first_surname: String,
    val user_second_surname: String,
    val user_phone: Long,
    val user_password: String,
    val user_email: String,
    val user_address: String,
    val user_city: String
)