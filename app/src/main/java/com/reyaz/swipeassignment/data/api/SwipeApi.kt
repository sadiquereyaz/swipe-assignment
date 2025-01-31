package com.reyaz.swipeassignment.data.api

import com.reyaz.swipeassignment.domain.Resource
import com.reyaz.swipeassignment.domain.model.Product
import com.reyaz.swipeassignment.domain.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SwipeApi {
    @GET("get")
    suspend fun getProducts(): Resource<List<Product>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: MultipartBody.Part?
    ): Resource<ProductResponse>
}