package com.reyaz.swipeassignment.data.api.dto

import com.google.gson.annotations.SerializedName
import com.reyaz.swipeassignment.domain.model.Product

data class ProductResponse(
    val message: String,
    @SerializedName("product_details")
    val details: Product,
    @SerializedName("product_id")
    val id: Int,
    val success: Boolean
)