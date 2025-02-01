package com.reyaz.swipeassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import com.reyaz.swipeassignment.data.db.entity.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadDao {
    @Insert
    suspend fun insertPendingUpload(upload: PendingUploadEntity)

    @Query("SELECT * FROM pending_uploads ORDER BY timestamp ASC")
     fun getActivePendingUploads(): List<PendingUploadEntity>

    @Delete
    suspend fun deletePendingUpload(upload: PendingUploadEntity)


    @Insert
    suspend fun insertProductNotification(product: NotificationEntity)

    @Query("UPDATE NotificationEntity SET status = :status WHERE productName = :productName")
    suspend fun updateProductStatus(status: Status, productName:String)

    @Query("UPDATE NotificationEntity SET isViewed = 1")
    suspend fun updateAllProductsAsViewed()

    @Query("SELECT * FROM NotificationEntity ORDER BY timestamp ASC")
    fun getAllNotification(): Flow<List<NotificationEntity>>
}
