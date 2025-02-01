package com.reyaz.swipeassignment.domain.model

import com.reyaz.swipeassignment.data.db.entity.ProductEntity

data class Product(
    val image: String? = null,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = 0,
            image = image,
            price = price,
            productName = product_name,
            productType = product_type,
            tax = tax,
        )
    }
}

data class ProductResponse(
    val message: String,
    val product_details: Product,
    val product_id: Int,
    val success: Boolean
)

