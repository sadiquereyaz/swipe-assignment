package com.reyaz.swipeassignment.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reyaz.swipeassignment.data.db.dao.UploadDao
import com.reyaz.swipeassignment.data.db.dao.ProductDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import com.reyaz.swipeassignment.data.db.entity.ProductEntity

@Database(
    entities = [ProductEntity::class, PendingUploadEntity::class, NotificationEntity::class],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun uploadDao(): UploadDao
}