package com.elshan.wallpick.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.elshan.wallpick.MainActivity
import com.elshan.wallpick.R
import com.elshan.wallpick.utils.datastore.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

const val NOTIFICATION_CHANNEL_ID = "CH-1"
const val NOTIFICATION_CHANNEL_NAME = "Login"
const val NOTIFICATION_ID = 1
const val REQUEST_CODE = 200

interface NotificationsService {
    suspend fun showNotification(value: String)
    fun createNotificationChannel()
    fun hideNotification()
    suspend fun showFCMNotification(title: String?, message: String?)
}

class NotificationsServiceImpl(
    private val context: Context,
) : NotificationsService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val myIntent = Intent(
        context, MainActivity::class.java
    )

    private val pendingIntent = PendingIntent.getActivity(
        context,
        REQUEST_CODE,
        myIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    override suspend fun showNotification(value: String) {
        val enabled = DataStoreManager.isNotificationsEnabled(context).first()

        if (enabled) {
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.wallpaper_app)
                .setContentTitle(value)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    override suspend fun showFCMNotification(title: String?, message: String?) {
        val enabled = DataStoreManager.isNotificationsEnabled(context).first()

        if (enabled && !title.isNullOrEmpty() && !message.isNullOrEmpty()) {
            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.wallpaper_app)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    override fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun hideNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }


}