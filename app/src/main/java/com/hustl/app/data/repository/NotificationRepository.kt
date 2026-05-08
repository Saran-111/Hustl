package com.hustl.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class HustlNotification(
    val id: String = "",
    val targetUserId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "general", // "order", "review"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

class NotificationRepository {
    private val db = FirebaseFirestore.getInstance()
    private val notifyCol = db.collection("notifications")
    
    // IMPORTANT: Paste the entire contents of your serviceAccountKey.json file between these quotes!
    private val SERVICE_ACCOUNT_JSON = """var admin = require("firebase-admin");

var serviceAccount = require("path/to/serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

    
    """

    suspend fun sendNotification(targetUserId: String, title: String, body: String, type: String): Result<Unit> {
        return try {
            val docRef = notifyCol.document()
            val notification = hashMapOf(
                "id" to docRef.id,
                "targetUserId" to targetUserId,
                "title" to title,
                "body" to body,
                "type" to type,
                "timestamp" to System.currentTimeMillis(),
                "isRead" to false,
                "isPushed" to false
            )
            docRef.set(notification).await()

            // Trigger true FCM Push Notification in the background using HTTP v1 API
            if (SERVICE_ACCOUNT_JSON.trim().isNotEmpty()) {
                sendFcmPushV1(targetUserId, title, body, type)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun sendFcmPushV1(targetUserId: String, title: String, body: String, type: String) {
        try {
            val userDoc = db.collection("users").document(targetUserId).get().await()
            val fcmToken = userDoc.getString("fcmToken")
            
            if (fcmToken.isNullOrEmpty()) return
            
            withContext(Dispatchers.IO) {
                // 1. Generate OAuth2 Token
                val json = JSONObject(SERVICE_ACCOUNT_JSON)
                val projectId = json.getString("project_id")
                val clientEmail = json.getString("client_email")
                val privateKeyString = json.getString("private_key")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("\\n", "")
                    .replace("\n", "")
                
                val header = Base64.encodeToString("""{"alg":"RS256","typ":"JWT"}""".toByteArray(), Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
                
                val iat = System.currentTimeMillis() / 1000
                val exp = iat + 3600
                val claimSet = """{"iss":"$clientEmail","scope":"https://www.googleapis.com/auth/firebase.messaging","aud":"https://oauth2.googleapis.com/token","exp":$exp,"iat":$iat}"""
                val payload = Base64.encodeToString(claimSet.toByteArray(), Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
                
                val keyBytes = Base64.decode(privateKeyString, Base64.DEFAULT)
                val spec = PKCS8EncodedKeySpec(keyBytes)
                val privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec)
                
                val signatureObj = Signature.getInstance("SHA256withRSA")
                signatureObj.initSign(privateKey)
                signatureObj.update("$header.$payload".toByteArray())
                val signature = Base64.encodeToString(signatureObj.sign(), Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
                
                val jwt = "$header.$payload.$signature"
                
                val authUrl = URL("https://oauth2.googleapis.com/token")
                val authConn = authUrl.openConnection() as HttpURLConnection
                authConn.requestMethod = "POST"
                authConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                authConn.doOutput = true
                authConn.outputStream.write("grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$jwt".toByteArray())
                
                val authResponse = authConn.inputStream.bufferedReader().readText()
                val accessToken = JSONObject(authResponse).getString("access_token")
                
                // 2. Send Push via FCM v1 API
                val fcmUrl = URL("https://fcm.googleapis.com/v1/projects/$projectId/messages:send")
                val fcmConn = fcmUrl.openConnection() as HttpURLConnection
                fcmConn.requestMethod = "POST"
                fcmConn.setRequestProperty("Content-Type", "application/json")
                fcmConn.setRequestProperty("Authorization", "Bearer $accessToken")
                fcmConn.doOutput = true
                
                val messagePayload = JSONObject().apply {
                    put("message", JSONObject().apply {
                        put("token", fcmToken)
                        put("notification", JSONObject().apply {
                            put("title", title)
                            put("body", body)
                        })
                        put("data", JSONObject().apply {
                            put("type", type)
                        })
                    })
                }
                
                fcmConn.outputStream.use { os ->
                    val input = messagePayload.toString().toByteArray(Charsets.UTF_8)
                    os.write(input, 0, input.size)
                }
                
                val responseCode = fcmConn.responseCode
                android.util.Log.d("HustlFCM", "FCM v1 Response: $responseCode")
            }
        } catch (e: Exception) {
            android.util.Log.e("HustlFCM", "FCM v1 Error: ${e.message}")
        }
    }

    fun listenForNotifications(userId: String): Flow<List<HustlNotification>> = callbackFlow {
        val listener = notifyCol
            .whereEqualTo("targetUserId", userId)
            // Removed isRead=false filter here to avoid requiring composite index
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val notifications = snapshot?.documents?.mapNotNull { doc -> 
                    val n = doc.toHustlNotification()
                    if (n != null && !n.isRead) n else null // Filter client-side
                } ?: emptyList()
                trySend(notifications)
            }
        awaitClose { listener.remove() }
    }

    fun getAllNotifications(userId: String): Flow<List<HustlNotification>> = callbackFlow {
        val listener = notifyCol
            .whereEqualTo("targetUserId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("Hustl", "Notify listener failed: ${error.message}")
                    return@addSnapshotListener
                }
                val notifications = snapshot?.documents?.mapNotNull { doc -> 
                    doc.toHustlNotification()
                } ?: emptyList()
                trySend(notifications)
            }
        awaitClose { listener.remove() }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toHustlNotification(): HustlNotification? {
        return try {
            val tsObj = get("timestamp")
            val ts = when (tsObj) {
                is Long -> tsObj
                is com.google.firebase.Timestamp -> tsObj.toDate().time
                else -> 0L
            }
            HustlNotification(
                id = id,
                targetUserId = getString("targetUserId") ?: "",
                title = getString("title") ?: "",
                body = getString("body") ?: "",
                type = getString("type") ?: "general",
                timestamp = ts,
                isRead = getBoolean("isRead") ?: false
            )
        } catch (e: Exception) { 
            android.util.Log.e("Hustl", "Failed mapping toHustlNotification: ${e.message}")
            null 
        }
    }

    suspend fun markAsRead(notificationId: String) {
        try {
            notifyCol.document(notificationId).update("isRead", true).await()
        } catch (_: Exception) {}
    }
}
