package com.reyaz.swipeassignment.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.reyaz.swipeassignment.data.db.dao.ProductDao
import com.reyaz.swipeassignment.data.db.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}