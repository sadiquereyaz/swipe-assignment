package com.reyaz.swipeassignment.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.reyaz.swipeassignment.R

class NotificationHelper(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Product Upload Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for product upload status"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showUploadProgressNotification(productName: String, message: String? = null) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.upload_progress)
            .setContentTitle("Uploading $productName")
            .setContentText(message ?: "Upload in progress...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setProgress(0, 0, true) // indeterminate progress

        notificationManager.notify(PROGRESS_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun hideProgressNotification() {
        notificationManager.cancel(PROGRESS_NOTIFICATION_ID)
    }


    fun showUploadSuccessNotification(productName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.upload_success)
            .setContentTitle("Product Upload Success")
            .setContentText("$productName has been uploaded successfully")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun showUploadFailureNotification(productName: String, error: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.upload_failed)
            .setContentTitle("Product Upload Failed")
            .setContentText("Failed to upload $productName: $error")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        private const val CHANNEL_ID = "product_upload_channel"
        private const val PROGRESS_NOTIFICATION_ID = 1001 // Unique ID for progress notifications
    }
}
