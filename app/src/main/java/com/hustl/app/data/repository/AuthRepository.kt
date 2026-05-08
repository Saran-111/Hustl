package com.hustl.app.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hustl.app.data.model.User
import com.hustl.app.data.model.toFirestoreMap
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    companion object {
        @Volatile private var cachedUser: User? = null
    }

    val currentUser: User? get() = cachedUser

    fun isHustlr(): Boolean = cachedUser?.role == "hustlr"

    // ─── Login ────────────────────────────────────────────────────────────────

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Authentication failed")
            val user = loadAndCacheUser(uid) ?: throw Exception("Profile not found. Please register again.")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    // ─── Register ─────────────────────────────────────────────────────────────

    suspend fun register(name: String, email: String, password: String, role: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Registration failed")

            val user = User(
                userId = uid,
                name = name,
                email = email,
                role = role,
                createdAt = System.currentTimeMillis()
            )

            db.collection("users").document(uid).set(user.toFirestoreMap()).await()
            cachedUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    // ─── Password Reset ───────────────────────────────────────────────────────

    suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    // ─── Session ──────────────────────────────────────────────────────────────

    suspend fun checkSession(): Boolean {
        val firebaseUser = auth.currentUser ?: return false
        return try {
            loadAndCacheUser(firebaseUser.uid) != null
        } catch (e: Exception) {
            false
        }
    }

    // ─── Profile ─────────────────────────────────────────────────────────────

    suspend fun getUserProfile(uid: String): User {
        return loadAndCacheUser(uid) ?: User()
    }

    suspend fun updateFcmToken(uid: String, token: String) {
        try {
            db.collection("users").document(uid)
                .update("fcmToken", token)
                .await()
        } catch (_: Exception) {}
    }

    suspend fun updateOnlineStatus(uid: String, online: Boolean) {
        try {
            db.collection("users").document(uid)
                .update(mapOf("isOnline" to online, "lastActive" to System.currentTimeMillis()))
                .await()
        } catch (_: Exception) {}
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private suspend fun loadAndCacheUser(uid: String): User? {
        val doc = db.collection("users").document(uid).get().await()
        if (!doc.exists()) return null
        val user = User(
            userId = doc.getString("userId") ?: uid,
            name = doc.getString("name") ?: "",
            email = doc.getString("email") ?: "",
            role = doc.getString("role") ?: "buyer",
            bio = doc.getString("bio") ?: "",
            profileImageUrl = doc.getString("profileImageUrl") ?: "",
            fcmToken = doc.getString("fcmToken") ?: "",
            rating = doc.getDouble("rating") ?: 0.0,
            reviewCount = (doc.getLong("reviewCount") ?: 0L).toInt(),
            totalOrders = (doc.getLong("totalOrders") ?: 0L).toInt(),
            location = doc.getString("location") ?: "",
            isOnline = doc.getBoolean("isOnline") ?: false,
            createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
        )
        if (auth.currentUser?.uid == user.userId) {
            cachedUser = user
        }
        return user
    }

    private fun mapFirebaseError(e: Exception): String {
        val msg = e.message ?: return "Something went wrong"
        return when {
            msg.contains("INVALID_LOGIN_CREDENTIALS") ||
            msg.contains("wrong-password") ||
            msg.contains("user-not-found") -> "Invalid email or password"
            msg.contains("email-already-in-use") ||
            msg.contains("EMAIL_EXISTS") -> "This email is already registered"
            msg.contains("weak-password") -> "Password must be at least 6 characters"
            msg.contains("invalid-email") ||
            msg.contains("INVALID_EMAIL") -> "Invalid email address"
            msg.contains("network") ||
            msg.contains("NETWORK") -> "No internet connection"
            msg.contains("too-many-requests") -> "Too many attempts. Try again later."
            else -> msg
        }
    }

    fun logout() {
        auth.signOut()
        cachedUser = null
    }

    fun isLoggedIn() = auth.currentUser != null
}
