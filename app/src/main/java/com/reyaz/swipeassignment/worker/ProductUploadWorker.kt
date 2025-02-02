package com.reyaz.swipeassignment.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.reyaz.swipeassignment.base.BaseApplication
import com.reyaz.swipeassignment.data.db.dao.PendingUploadDao
import com.reyaz.swipeassignment.domain.model.Resource
import com.reyaz.swipeassignment.domain.repository.ProductRepository
import com.reyaz.swipeassignment.utils.NotificationHelper

class ProductUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val notificationHelper = NotificationHelper(context)
    private val repository: ProductRepository by lazy {
        (context.applicationContext as BaseApplication).getKoin().get()
    }

    private val pendingUploadDao: PendingUploadDao by lazy {
        (context.applicationContext as BaseApplication).getKoin().get()
    }

    override suspend fun doWork(): Result {
        try {
            pendingUploadDao.getAll().forEach { pendingUpload ->
                notificationHelper.showUploadProgressNotification(pendingUpload.productName)
                val result = repository.addProduct(
                    productName = pendingUpload.productName,
                    productType = pendingUpload.productType,
                    price = pendingUpload.price,
                    tax = pendingUpload.tax,
                    imageUri = pendingUpload.imageUri?.let { Uri.parse(it) },
                    isForeground = false
                )
                when (result) {
                    is Resource.Success -> {
                        pendingUploadDao.delete(pendingUpload)
                    }
                    is Resource.Error -> {
                        Log.e("WORKER", "Upload failed for ${pendingUpload.productName}")
                    }
                    else -> Log.d("WORKER", "Unexpected state during upload")
                }
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e("WORKER", "Error during product upload: ${e.message}")
            return Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "product_upload_work"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val uploadWorkRequest = OneTimeWorkRequestBuilder<ProductUploadWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                uploadWorkRequest
            )
        }
    }
}
