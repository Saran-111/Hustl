package com.hustl.app.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HustlFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Handle push notifications here
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Save token to Firestore for the current user
    }
}
