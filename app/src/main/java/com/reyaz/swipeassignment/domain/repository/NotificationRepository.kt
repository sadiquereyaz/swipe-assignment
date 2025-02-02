package com.reyaz.swipeassignment.domain.repository

import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAll(): Flow<Resource<List<NotificationEntity>>>
    suspend fun markAsViewed()
}