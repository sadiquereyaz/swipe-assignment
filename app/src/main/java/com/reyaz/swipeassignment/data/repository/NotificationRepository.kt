package com.reyaz.swipeassignment.data.repository

import android.util.Log
import com.reyaz.swipeassignment.data.db.dao.UploadDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationRepository(
    private val uploadDao: UploadDao
) {
    fun getAll(): Flow<Resource<List<NotificationEntity>>> = flow {
        Log.d("REPOSITORY", "getAllProducts()")
        emit(Resource.Loading())
        try {
            uploadDao.getAllNotification().collect { products ->
                emit(Resource.Success(products))
            }
            markAsViewed()
        } catch (e: Exception) {
            Log.d("REPOSITORY", "getAllProducts() ERRor ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun markAsViewed() {
        uploadDao.updateAllProductsAsViewed()
    }
}
