package com.example.appinterface.Api.Models

data class OutputOrder(
    val out_order_id: Int,
    val out_order_date: String,
    val output_details_id: Int,
    val product_serial: String,
    val out_product_garanty: String,
    val product_transformation: String,
    val product_detail_description: String,
    val product_detail_model: String,
    val product_brand_name: String,
    val out_order_status: Int
)

data class CreateOutputOrder(
    val product_serial: String,
    val product_transformation: String,
    val out_product_garanty: String,
)

data class UpdateOutputOrder(
    val out_order_id: Int,
    val product_serial: String,
    val product_transformation: String,
    val out_product_garanty: String,
    val out_order_status: Int,
)