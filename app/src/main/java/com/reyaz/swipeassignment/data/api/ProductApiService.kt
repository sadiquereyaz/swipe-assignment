/*
package com.reyaz.swipeassignment.data.api

import com.reyaz.swipeassignment.domain.model.AddProductResponse
import com.reyaz.swipeassignment.domain.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApiService {
    @GET("public/get")
    suspend fun getProducts(): List<Product>

    @Multipart
    @POST("public/add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: List<MultipartBody.Part>?
    ): AddProductResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://app.getswipe.in/api/"

    val instance: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}*/
