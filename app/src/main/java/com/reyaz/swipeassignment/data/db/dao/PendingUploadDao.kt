package com.reyaz.swipeassignment.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity

@Dao
interface PendingUploadDao {
    @Insert
    suspend fun insert(upload: PendingUploadEntity)

    @Query("SELECT * FROM pending_uploads ORDER BY timestamp ASC")
     fun getAll(): List<PendingUploadEntity>

    @Delete
    suspend fun delete(upload: PendingUploadEntity)

}
