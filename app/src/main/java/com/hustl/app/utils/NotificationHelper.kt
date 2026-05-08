package com.hustl.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hustl.app.R
import com.hustl.app.ui.home.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ORDERS = "hustl_orders"
        const val CHANNEL_REVIEWS = "hustl_reviews"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val orderChannel = NotificationChannel(
                CHANNEL_ORDERS, "Orders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Order status updates" }

            val reviewChannel = NotificationChannel(
                CHANNEL_REVIEWS, "Reviews",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "New review notifications" }

            nm.createNotificationChannels(listOf(orderChannel, reviewChannel))
        }
    }

    fun showNotification(title: String, body: String, channelId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_bell) 
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(System.currentTimeMillis().toInt(), notification)
    }
}
