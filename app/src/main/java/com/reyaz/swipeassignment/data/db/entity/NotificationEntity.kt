package com.reyaz.swipeassignment.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reyaz.swipeassignment.domain.model.Status

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val productType: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isViewed: Boolean = false,
    val status: Status = Status.Pending
)