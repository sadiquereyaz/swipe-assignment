package com.reyaz.swipeassignment.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.reyaz.swipeassignment.data.api.SwipeApi
import com.reyaz.swipeassignment.data.db.dao.NotificationDao
import com.reyaz.swipeassignment.data.db.dao.PendingUploadDao
import com.reyaz.swipeassignment.data.db.dao.ProductDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import com.reyaz.swipeassignment.domain.model.Resource
import com.reyaz.swipeassignment.domain.model.Status
import com.reyaz.swipeassignment.domain.repository.ProductRepository
import com.reyaz.swipeassignment.utils.NotificationHelper
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
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class ProductRepositoryImpl(
    private val api: SwipeApi,
    private val productDao: ProductDao,
    private val pendingUploadDao: PendingUploadDao,
    private val notificationDao: NotificationDao,
    private val context: Context,
    private val notificationHelper: NotificationHelper
) : ProductRepository {

    override fun getAllProducts(): Flow<Resource<List<ProductEntity>>> = flow {
        emit(Resource.Loading())
        try {
            if (isOnline(context)) refreshProducts()
            productDao.getAllProducts().collect { products ->
                emit(Resource.Success(products))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun refreshProducts() {
        try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                productDao.deleteAllProducts()
                response.body()?.forEach { product ->
                    productDao.insertProduct(product.toEntity())
                }
            }
        } catch (e: Exception) {
            Log.e("REPOSITORY", "refreshProducts error: ${e.message}")
        }
    }

    override suspend fun addProduct(
        productName: String,
        productType: String,
        price: Double,
        tax: Double,
        imageUri: Uri?,
        isForeground: Boolean,
    ): Resource<Unit> {
        return try {
            if (!isOnline(context)) {
                savePendingUpload(productName, productType, price, tax, imageUri)
                return Resource.Success(Unit)
            }

            notificationHelper.showUploadProgressNotification(productName)
            if (notificationDao.getNotificationByProductName(productName) == 0) {
                notificationDao.insertProductNotification(
                    NotificationEntity(
                        productType = productType,
                        productName = productName,
                        status = Status.Pending
                    )
                )
            }

            val requestBodyMap = mutableMapOf<String, RequestBody>()
            requestBodyMap["product_name"] =
                productName.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["product_type"] =
                productType.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["price"] =
                price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            requestBodyMap["tax"] = tax.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart =
                if (!isForeground)
                    imageUri?.let { createImagePart(Uri.parse(it.toString()).path.toString()) }
                else
                    imageUri?.let { createImagePart(it) }

            val response = api.addProduct(requestBodyMap, imagePart)
            if (response.isSuccessful) {
                notificationDao.updateProductStatus(
                    productName = productName,
                    status = Status.Uploaded,
                    isViewed = false
                )
                notificationHelper.hideProgressNotification()
                notificationHelper.showUploadSuccessNotification(productName)
                Resource.Success(Unit)
            } else {
                notificationHelper.hideProgressNotification()
                notificationHelper.showUploadFailureNotification(productName, response.message())
                Resource.Error(response.message() ?: "Error while adding the product")
            }
        } catch (e: Exception) {
            notificationHelper.hideProgressNotification()
            notificationHelper.showUploadFailureNotification(
                productName,
                e.localizedMessage ?: "Unknown error"
            )
            notificationDao.updateProductStatus(
                productName = productName,
                status = Status.Failed,
                isViewed = false
            )
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part? {
        Log.d("REPOSITORY", "createImagePart")
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            Log.d("REPOSITORY", "returning createImagePart")
            return MultipartBody.Part.createFormData("files[]", tempFile.name, requestFile)
        } catch (e: Exception) {
            Log.d("REPOSITORY", "createImagePart error: ${e.message}")
            return null
        }
    }

    private suspend fun savePendingUpload(
        productName: String,
        productType: String,
        price: Double,
        tax: Double,
        imageUri: Uri?
    ) {
        val imagePath = imageUri?.let { persistImage(it) }
        pendingUploadDao.insert(
            PendingUploadEntity(
                productName = productName,
                productType = productType,
                price = price,
                tax = tax,
                imageUri = imagePath
            )
        )
        notificationDao.insertProductNotification(
            NotificationEntity(
                productType = productType,
                productName = productName,
                status = Status.Pending
            )
        )
        notificationHelper.showUploadProgressNotification(
            productName,
            "Waiting for the internet connection"
        )
        ProductUploadWorker.schedule(context)
    }

    private fun persistImage(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "image_${UUID.randomUUID()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file.absolutePath
        } catch (e: Exception) {
            Log.e("REPOSITORY", "Error persisting image: ${e.message}")
            null
        }
    }

    private fun createImagePart(filePath: String): MultipartBody.Part? {
        return try {
            val file = File(filePath)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files[]", file.name, requestFile)
        } catch (e: Exception) {
            Log.e("REPOSITORY", "createImagePart error: ${e.message}")
            null
        }
    }

    override fun getUnViewedCount(): Flow<Int> = flow {
        notificationDao.getUnViewedCount().collect { emit(it) }
    }
}
