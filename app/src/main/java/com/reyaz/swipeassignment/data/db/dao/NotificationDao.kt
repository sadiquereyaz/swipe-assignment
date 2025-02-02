package com.reyaz.swipeassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.domain.model.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductNotification(product: NotificationEntity)

    @Query("SELECT COUNT(*) FROM NotificationEntity WHERE productName = :productName")
    suspend fun getNotificationByProductName(productName: String): Int

    @Query("UPDATE NotificationEntity SET status = :status, isViewed = :isViewed WHERE productName = :productName")
    suspend fun updateProductStatus(status: Status, productName:String, isViewed: Boolean = false)

    @Query("UPDATE NotificationEntity SET isViewed = 1")
    suspend fun updateAllProductsAsViewed()

    @Query("SELECT COUNT(*) FROM NotificationEntity WHERE isViewed = 0")
    fun getUnViewedCount(): Flow<Int>

    @Query("SELECT * FROM NotificationEntity ORDER BY timestamp DESC")
    fun getAllNotification(): Flow<List<NotificationEntity>>
}
