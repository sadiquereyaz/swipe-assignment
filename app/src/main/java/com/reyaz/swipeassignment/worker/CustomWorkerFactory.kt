package com.reyaz.swipeassignment.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

/*class CustomWorkerFactory(
    private val repository: ProductRepository,
    private val uploadDao: UploadDao
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            ProductUploadWorker::class.java.name ->
                ProductUploadWorker(context = appContext, params=workerParameters, repository = repository, uploadDao = uploadDao)
            else ->
                null
        }
    }
}*/
class CustomWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            ProductUploadWorker::class.java.name -> ProductUploadWorker(appContext, workerParameters)
            else -> null
        }
    }
}

