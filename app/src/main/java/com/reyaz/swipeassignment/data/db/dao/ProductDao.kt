package com.reyaz.swipeassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM products WHERE isPending = 1")
    suspend fun getPendingProducts(): List<ProductEntity>

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

}