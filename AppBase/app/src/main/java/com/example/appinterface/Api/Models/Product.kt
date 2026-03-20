package com.example.appinterface.Api.Models


data class Product (
    var input_order_id: Int? = null,
    var input_order_bill: String? = "",
    var category_name: String? = "",
    var subcategory_id: Int? = null,
    var subcategory_name: String? = "",
    var product_id : Int? = null,
    var product_serial: String? = "",
    var product_model: String? = "",
    var product_details_id: Int? = null,
    var product_garanty_input: String? = ""
)
