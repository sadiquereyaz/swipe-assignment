
import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.reyaz.swipeassignment.data.db.dao.PendingUploadDao
import com.reyaz.swipeassignment.data.repository.ProductRepository
import com.reyaz.swipeassignment.domain.Resource
import com.reyaz.swipeassignment.utils.NotificationHelper

class ProductUploadWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ProductRepository,
    private val pendingUploadDao: PendingUploadDao
) : CoroutineWorker(context, params) {

    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {
        try {
            val pendingUploads = pendingUploadDao.getAllPendingUploads()
2
            pendingUploads.forEach { pendingUpload ->
                val result = repository.addProduct(
                    productName = pendingUpload.productName,
                    productType = pendingUpload.productType,
                    price = pendingUpload.price,
                    tax = pendingUpload.tax,
                    imageUri = pendingUpload.imageUri?.let { Uri.parse(it) }
                )

                when (result) {
                    is Resource.Success -> {
                        pendingUploadDao.deletePendingUpload(pendingUpload)
                        notificationHelper.hideProgressNotification()
                        notificationHelper.showUploadSuccessNotification(pendingUpload.productName)
                    }
                    is Resource.Error -> {
                        notificationHelper.showUploadFailureNotification(
                            pendingUpload.productName,
                            result.message ?: "Unknown error"
                        )
                    }
                    is Resource.Loading -> {
                        notificationHelper.showUploadProgressNotification(pendingUpload.productName)
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
}