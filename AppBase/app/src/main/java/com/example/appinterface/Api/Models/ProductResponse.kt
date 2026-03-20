package com.example.appinterface.RESPONSE

import com.example.appinterface.Api.Models.Product

data class ProductResponse(

    val success: Boolean,
    val message: String,
    val data: List<Product>
)