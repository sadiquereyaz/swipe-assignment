package com.reyaz.swipeassignment.domain.model

import com.google.gson.annotations.SerializedName
import com.reyaz.swipeassignment.data.db.entity.ProductEntity

data class Product(
    val image: String? = null,
    val price: Double,
    @SerializedName("product_name")
    val name: String,
    @SerializedName("product_type")
    val type: String,
    val tax: Double
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = 0,
            image = image,
            price = price,
            productName = name,
            productType = type,
            tax = tax,
        )
    }
}

