


package com.reyaz.swipeassignment.di

import androidx.room.Room
import com.reyaz.swipeassignment.data.api.SwipeApi
import com.reyaz.swipeassignment.data.db.AppDatabase
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.ui.ProductViewModel
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
        ).build()
    }
    single { get<AppDatabase>().productDao() }

    // Repository
    single { ProductRepository(get(), get()) }

    // ViewModels
    viewModel { ProductViewModel(get()) }
}