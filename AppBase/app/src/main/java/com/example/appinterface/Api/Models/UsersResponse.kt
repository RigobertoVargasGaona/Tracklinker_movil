package com.example.appinterface.Api.Models

data class UsersResponse (
    val success: Boolean,
    val data: List<User>
) {
}