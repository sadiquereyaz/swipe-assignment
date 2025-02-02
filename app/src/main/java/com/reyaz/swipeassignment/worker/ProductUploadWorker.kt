package com.reyaz.swipeassignment.worker
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.reyaz.swipeassignment.BaseApplication
import com.reyaz.swipeassignment.data.db.dao.UploadDao
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.Status
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.domain.Resource

class ProductUploadWorker(
    context: Context,
    params: WorkerParameters,
//    private val repository: ProductRepository,
//    private val uploadDao: UploadDao
) : CoroutineWorker(context, params) {

//    private val notificationHelper = NotificationHelper(context)
    private val repository: ProductRepository by lazy {
        // Retrieve from a global dependency injector or pass via DI
        (context.applicationContext as BaseApplication).getKoin().get()
    }

    private val uploadDao: UploadDao by lazy {
        (context.applicationContext as BaseApplication).getKoin().get()
    }

    override suspend fun doWork(): Result {
        try {
            Log.d("WORKER", "doWork() as internet connected")
            uploadDao.getActivePendingUploads().forEach { pendingUpload ->
                val result = repository.addProduct(
                    productName = pendingUpload.productName,
                    productType = pendingUpload.productType,
                    price = pendingUpload.price,
                    tax = pendingUpload.tax,
                    imageUri = pendingUpload.imageUri?.let { Uri.parse(it) }
                )

                when (result) {
                    is Resource.Success -> {
                        Log.d("WORKER", "doWork() success")
                        uploadDao.updateProductStatus(Status.Uploaded, productName = pendingUpload.productName)
                        uploadDao.deletePendingUpload(pendingUpload)
                        uploadDao.insertProductNotification(
                            NotificationEntity(
                                productName = pendingUpload.productName,
                                productType = pendingUpload.productType,
                                status = Status.Uploaded,
                                isViewed = false
                            )
                        )
//                        notificationHelper.hideProgressNotification()
//                        notificationHelper.showUploadSuccessNotification(pendingUpload.productName)
                    }
                    is Resource.Error -> {
                        Log.d("WORKER", "inside doWork() add response error")
                        uploadDao.updateProductStatus(Status.Failed, productName = pendingUpload.productName)
//                        notificationHelper.showUploadFailureNotification(
//                            pendingUpload.productName,
//                            result.message ?: "Unknown error"
//                        )
                    }
                    is Resource.Loading -> {
                        Log.d("WORKER", "doWork() loading")
//                        notificationHelper.showUploadProgressNotification(pendingUpload.productName)
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "product_upload_work"

        fun schedule(context: Context) {
            Log.d("WORKER", "schedule()")
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val uploadWorkRequest = OneTimeWorkRequestBuilder<ProductUploadWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    UNIQUE_WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    uploadWorkRequest
                )
        }
    }
    /*class Factory(
        private val repository: ProductRepository,
        private val uploadDao: UploadDao
    ) {
        fun create(context: Context, params: WorkerParameters): ProductUploadWorker {
            return ProductUploadWorker(context, params, repository, uploadDao)
        }
    }*/
}