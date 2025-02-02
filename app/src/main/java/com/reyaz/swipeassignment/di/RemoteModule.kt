package com.reyaz.swipeassignment.di

import com.reyaz.swipeassignment.data.api.SwipeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createRetrofitInstance(): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://app.getswipe.in/api/public/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val remoteModule = module {
    single { createRetrofitInstance() }
    single { get<Retrofit>().create(SwipeApi::class.java) }
}