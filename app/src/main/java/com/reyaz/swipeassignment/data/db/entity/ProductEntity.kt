package com.reyaz.swipeassignment.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val image: String?,
    val price: Double,
    val productName: String,
    val productType: String,
    val tax: Double,
    val isPending: Boolean = false
)