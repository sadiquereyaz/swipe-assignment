package com.reyaz.swipeassignment.data.repository

import android.util.Log
import com.reyaz.swipeassignment.data.db.dao.NotificationDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.domain.model.Resource
import com.reyaz.swipeassignment.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao
) : NotificationRepository {    override fun getAll(): Flow<Resource<List<NotificationEntity>>> = flow {
        Log.d("REPOSITORY", "getAllProducts()")
        emit(Resource.Loading())
        try {
            notificationDao.getAllNotification().collect { products ->
                emit(Resource.Success(products))
            }
        } catch (e: Exception) {
            Log.d("REPOSITORY", "getAllProducts() ERRor ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun markAsViewed() {
        Log.d("REPOSITORY", "markAsViewed() executed")
        notificationDao.updateAllProductsAsViewed()
    }
}
