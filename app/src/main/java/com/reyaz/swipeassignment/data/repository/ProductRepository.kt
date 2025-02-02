package com.reyaz.swipeassignment.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.reyaz.swipeassignment.data.api.SwipeApi
import com.reyaz.swipeassignment.data.db.dao.ProductDao
import com.reyaz.swipeassignment.data.db.dao.UploadDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.data.db.entity.Status
import com.reyaz.swipeassignment.domain.Resource
import com.reyaz.swipeassignment.utils.isOnline
import com.reyaz.swipeassignment.worker.ProductUploadWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository(
    private val api: SwipeApi,
    private val productDao: ProductDao,
    private val uploadDao: UploadDao,
    private val context: Context
) {
    fun getAllProducts(): Flow<Resource<List<ProductEntity>>> = flow {
        Log.d("REPOSITORY", "getAllProducts()")
        emit(Resource.Loading())
        try {
            if (isOnline(context)) refreshProducts()
            productDao.getAllProducts().collect { products ->
                emit(Resource.Success(products))
            }
        } catch (e: Exception) {
            Log.d("REPOSITORY", "getAllProducts() ERRor ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun refreshProducts() {
        Log.d("REPOSITORY", "refresh()")
        try {
            val response = api.getProducts()
            //Log.d("REPOSITORY", "product response ${response.body()}")
            if (response.isSuccessful) {
                productDao.deleteAllProducts()
                response.body()?.forEach { product ->
                    // if (product.image != "")
                    productDao.insertProduct(product.toEntity())
                }
            } else {
                Log.e("REPOSITORY", "Error fetching products: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("REPOSITORY", "refreshProducts error: ${e.message}")
        }
    }


    suspend fun addProduct(
        productName: String,
        productType: String,
        price: Double,
        tax: Double,
        imageUri: Uri?
    ): Resource<Unit> {
        return try {
            if (!isOnline(context)) {
                Log.d("REPOSITORY", "addProduct offline")
                // Save to pending uploads
                uploadDao.insertPendingUpload(
                    PendingUploadEntity(
                        productName = productName,
                        productType = productType,
                        price = price,
                        tax = tax,
                        imageUri = imageUri?.toString()
                    )
                )
                uploadDao.insertProductNotification(
                    NotificationEntity(
                        productType = productType,
                        productName = productName,
                        status = Status.Pending
                    )
                )
                // Schedule upload work
                ProductUploadWorker.schedule(context)

                return Resource.Success(Unit)
            }
            Log.d("REPOSITORY", "addProduct notification above")

            if (uploadDao.getNotificationByProductName(productName) == 0) {
                Log.d("REPOSITORY", "$productName is new product")
                uploadDao.insertProductNotification(
                    NotificationEntity(
                        productType = productType,
                        productName = productName,
                        status = Status.Pending
                    )
                )
            } else
                Log.d("REPOSITORY", "$productName already exists")

            val requestBodyMap = mutableMapOf<String, RequestBody>()
            requestBodyMap["product_name"] =
                productName.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["product_type"] =
                productType.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["price"] =
                price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["tax"] = tax.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageUri?.let { createImagePart(it) }

            val response = api.addProduct(requestBodyMap, imagePart)
            Log.d("REPOSITORY", "addProduct requested")
            if (response.isSuccessful) {
                Log.d("REPOSITORY", "addProduct success: ${response.body()}")
                uploadDao.updateProductStatus(
                    productName = productName,
                    status = Status.Uploaded
                )
                Resource.Success(Unit)

            } else {
                Log.d("REPOSITORY", "addProduct error: ${response.message()}")
                Resource.Error(
                    response.message() ?: "Error while adding the product"
                )
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.d("REPOSITORY", "addProduct error: ${e.message}")
            uploadDao.updateProductStatus(
                productName = productName,
                status = Status.Failed
            )
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    // Create text fields as RequestBody
    fun createPartFromString(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part? {
        Log.d("REPOSITORY", "createImagePart")
        try{
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            Log.d("REPOSITORY", "returning createImagePart")
            return MultipartBody.Part.createFormData("files[]", tempFile.name, requestFile)
        } catch(e:Exception){
            Log.d("REPOSITORY", "createImagePart error: ${e.message}")
            return null
        }
    }

    fun getUnviewedCount(): Flow<Int> {
        return uploadDao.getUnviewedCount()
    }
}
