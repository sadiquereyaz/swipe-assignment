package com.reyaz.swipeassignment.domain.repository

import android.net.Uri
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<Resource<List<ProductEntity>>>
    suspend fun addProduct(
        productName: String,
        productType: String,
        price: Double,
        tax: Double,
        imageUri: Uri?,
        isForeground: Boolean
    ): Resource<Unit>
    fun getUnViewedCount(): Flow<Int>
}