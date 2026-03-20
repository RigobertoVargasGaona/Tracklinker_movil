package com.example.appinterface.Api.Models

data class DataResponseWarranty(
    val warranty_incidents_id: Int?,
    val product_serial: String = "",
    val warranty_customer: String = "",
    val warranty_phone: String = "",
    val warranty_address: String = "",
    val warranty_description: String = "",
    val warranty_link_attachments: String? = null,
    val warranty_city: String = "",
    val warranty_status: String = ""
)