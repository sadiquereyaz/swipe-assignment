package com.reyaz.swipeassignment.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_uploads")
data class PendingUploadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val productType: String,
    val price: Double,
    val tax: Double,
    val imageUri: String?,
    val timestamp: Long = System.currentTimeMillis()
)