package com.example.appinterface.Api.Models

data class Category(
    val category_id: Int,
    val category_name: String,
    val category_date: String? = null,
)

data class CreateCategory(
    val category_name: String,
)