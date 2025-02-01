package com.reyaz.swipeassignment.data.db.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

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

enum class Status(val color: Color) {
    Pending(Color(0xFF028A00)), Uploaded(Color(0xFFFFC107)), Failed(Color(0xFF2196F3))
}