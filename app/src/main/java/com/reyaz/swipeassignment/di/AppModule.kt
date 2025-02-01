package com.reyaz.swipeassignment.di

import androidx.room.Room
import com.reyaz.swipeassignment.data.api.SwipeApi
import com.reyaz.swipeassignment.data.db.AppDatabase
import com.reyaz.swipeassignment.data.repository.NotificationRepository
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.presentation.notification.NotificationViewModel
import com.reyaz.swipeassignment.presentation.product.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    // API
    single { createRetrofitInstance() }
    single { get<Retrofit>().create(SwipeApi::class.java) }

    // Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "swipe-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().uploadDao() }

    // Repository
    single { ProductRepository(get(), get(),get(), get()) }
    single { NotificationRepository(get()) }

    // ViewModels
    viewModel { ProductViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
}