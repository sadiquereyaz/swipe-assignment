package com.reyaz.swipeassignment.presentation.notification

import com.reyaz.swipeassignment.data.db.entity.NotificationEntity

data class NotificationUiState(
    val notificationList: List<NotificationEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)