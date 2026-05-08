package com.hustl.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// NOTE: @Entity annotations kept so Room cache layer compiles.
// Password is intentionally absent — Firebase Auth manages credentials.

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "buyer",          // "buyer" or "hustlr"
    val bio: String = "",
    val profileImageUrl: String = "",
    val fcmToken: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val totalOrders: Int = 0,
    val location: String = "",
    val isOnline: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gigs")
data class Gig(
    @PrimaryKey val gigId: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val sellerImageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val minPrice: Int = 0,
    val maxPrice: Int = 0,
    val deliveryDays: Int = 3,
    val revisions: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String = "",
    val gigId: String = "",
    val gigTitle: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val packageName: String = "",
    val price: Int = 0,
    val status: String = "pending",
    val requirements: String = "",
    val progress: Int = 0,
    val revisionNote: String = "",
    val ratingGiven: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveryDate: Long = 0L
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val attachmentUrl: String = "",
    val type: String = "text",
    val seen: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey val chatId: String = "",
    val participants: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val gigId: String = "",
    val gigTitle: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val reviewId: String = "",
    val gigId: String = "",
    val orderId: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val reviewedUserId: String = "",
    val rating: Float = 5f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// ─── Firestore helpers ────────────────────────────────────────────────────────

fun User.toFirestoreMap(): Map<String, Any> = mapOf(
    "userId" to userId,
    "name" to name,
    "email" to email,
    "role" to role,
    "bio" to bio,
    "profileImageUrl" to profileImageUrl,
    "fcmToken" to fcmToken,
    "rating" to rating,
    "reviewCount" to reviewCount,
    "totalOrders" to totalOrders,
    "location" to location,
    "isOnline" to isOnline,
    "createdAt" to createdAt
)

fun Gig.toFirestoreMap(): Map<String, Any> = mapOf(
    "gigId" to gigId,
    "sellerId" to sellerId,
    "sellerName" to sellerName,
    "sellerImageUrl" to sellerImageUrl,
    "title" to title,
    "description" to description,
    "category" to category,
    "tags" to tags,
    "imageUrl" to imageUrl,
    "rating" to rating,
    "reviewCount" to reviewCount,
    "minPrice" to minPrice,
    "maxPrice" to maxPrice,
    "deliveryDays" to deliveryDays,
    "revisions" to revisions,
    "createdAt" to createdAt,
    "isActive" to isActive
)

fun Order.toFirestoreMap(): Map<String, Any> = mapOf(
    "orderId" to orderId,
    "gigId" to gigId,
    "gigTitle" to gigTitle,
    "buyerId" to buyerId,
    "buyerName" to buyerName,
    "sellerId" to sellerId,
    "sellerName" to sellerName,
    "packageName" to packageName,
    "price" to price,
    "status" to status,
    "requirements" to requirements,
    "progress" to progress,
    "revisionNote" to revisionNote,
    "ratingGiven" to ratingGiven,
    "createdAt" to createdAt,
    "deliveryDate" to deliveryDate
)

fun Message.toFirestoreMap(): Map<String, Any> = mapOf(
    "messageId" to messageId,
    "chatId" to chatId,
    "senderId" to senderId,
    "senderName" to senderName,
    "text" to text,
    "attachmentUrl" to attachmentUrl,
    "type" to type,
    "seen" to seen,
    "timestamp" to timestamp
)

// ─── Room TypeConverters (kept for cache layer) ────────────────────────────

class Converters {
    private val gson = Gson()

    @TypeConverter fun fromStringList(v: List<String>): String = gson.toJson(v)
    @TypeConverter fun toStringList(v: String): List<String> = gson.fromJson(v, object : TypeToken<List<String>>() {}.type)

    @TypeConverter fun fromStringMap(v: Map<String, String>): String = gson.toJson(v)
    @TypeConverter fun toStringMap(v: String): Map<String, String> = gson.fromJson(v, object : TypeToken<Map<String, String>>() {}.type)
}
