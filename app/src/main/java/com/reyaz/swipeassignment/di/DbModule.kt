package com.reyaz.swipeassignment.di

import androidx.room.Room
import com.reyaz.swipeassignment.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "swipe-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().pendingUploadDao() }
    single { get<AppDatabase>().notificationDao() }

}