package com.hustl.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hustl.app.R
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.ui.home.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HustlFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_MESSAGES = "hustl_messages"
        const val CHANNEL_ORDERS = "hustl_orders"
        const val CHANNEL_GENERAL = "hustl_general"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Save FCM token to Firestore for this user
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authRepo = AuthRepository(applicationContext)
                val uid = authRepo.currentUser?.userId ?: return@launch
                authRepo.updateFcmToken(uid, token)
            } catch (_: Exception) {}
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "HustL"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: ""
        val type = remoteMessage.data["type"] ?: "general"
        val chatId = remoteMessage.data["chatId"] ?: ""

        val channelId = when (type) {
            "message" -> CHANNEL_MESSAGES
            "order" -> CHANNEL_ORDERS
            else -> CHANNEL_GENERAL
        }

        showNotification(title, body, channelId, chatId)
    }

    private fun showNotification(title: String, body: String, channelId: String, chatId: String) {
        createNotificationChannels()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (chatId.isNotEmpty()) putExtra("open_chat_id", chatId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelMessages = NotificationChannel(
                CHANNEL_MESSAGES, "Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "New message notifications" }

            val channelOrders = NotificationChannel(
                CHANNEL_ORDERS, "Orders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Order status updates" }

            val channelGeneral = NotificationChannel(
                CHANNEL_GENERAL, "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "General app notifications" }

            nm.createNotificationChannels(listOf(channelMessages, channelOrders, channelGeneral))
        }
    }
}
