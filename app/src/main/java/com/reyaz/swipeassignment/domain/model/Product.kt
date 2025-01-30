package com.reyaz.swipeassignment.domain.model

data class Product(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)

data class AddProductResponse(
    val message: String,
    val product_details: Product,
    val product_id: Int,
    val success: Boolean
)