package com.example.appinterface.Api.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataResponseWarranty(
    val warranty_incidents_id: Int? = null,
    val product_serial: String = "",
    val warranty_customer: String = "",
    val warranty_phone: String = "",
    val warranty_address: String = "",
    val warranty_description: String = "",
    val warranty_link_attachments: String? = null,
    val warranty_city: String = "",
    val warranty_status: String = "",

    // serialize = false: NO se envía en el POST/PUT (evita el error de Spring)
    // deserialize = true: SÍ se recibe en el GET (para mostrarla en el RecyclerView)
    @Expose(serialize = false, deserialize = true)
    val warranty_date: String? = null
)