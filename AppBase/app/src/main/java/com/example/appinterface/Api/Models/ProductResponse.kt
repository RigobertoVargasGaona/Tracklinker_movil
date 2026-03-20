package com.example.appinterface.RESPONSE

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("id")
    val id: Int? = null
)