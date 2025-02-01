package com.reyaz.swipeassignment.data.api

import com.reyaz.swipeassignment.domain.Resource
import com.reyaz.swipeassignment.domain.model.Product
import com.reyaz.swipeassignment.domain.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface SwipeApi {
    @GET("get")
    suspend fun getProducts(): Response<List<Product>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @PartMap productData: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Response<ProductResponse>

    @Multipart
    @POST("api/public/add")
    fun uploadProductData(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part image: List<MultipartBody.Part>?  = emptyList()
    ): Call<ProductResponse>
}