package com.reyaz.swipeassignment.di

import androidx.work.Configuration
import com.reyaz.swipeassignment.domain.repository.NotificationRepository
import com.reyaz.swipeassignment.data.repository.NotificationRepositoryImpl
import com.reyaz.swipeassignment.domain.repository.ProductRepository
import com.reyaz.swipeassignment.data.repository.ProductRepositoryImpl
import com.reyaz.swipeassignment.presentation.notification.NotificationViewModel
import com.reyaz.swipeassignment.presentation.product.ProductViewModel
import com.reyaz.swipeassignment.utils.NotificationHelper
import com.reyaz.swipeassignment.worker.CustomWorkerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { NotificationHelper(androidContext()) }

    // Repository
    single <ProductRepository> { ProductRepositoryImpl(get(), get(), get(), get(), androidContext(), get()) }
    single <NotificationRepository> { NotificationRepositoryImpl(get()) }

    // Custom WorkerFactory
   /* single {
        CustomWorkerFactory(
            repository = get(),
            uploadDao = get()
        )
    }*/

    single {
        Configuration.Builder()
            .setWorkerFactory(get<CustomWorkerFactory>())
            .build()
    }

    // ViewModels
    viewModelOf(::ProductViewModel)
    viewModelOf(::NotificationViewModel)
}