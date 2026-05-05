package com.hustl.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "buyer",
    val bio: String = "",
    val profileImageUrl: String = "",
    val rating: Double = 0.0,
    val totalOrders: Int = 0,
    val location: String = "",
    val walletBalance: Int = 0, // Added for Hustl Wallet
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
    val packages: List<GigPackage> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class GigPackage(
    val name: String = "",
    val price: Int = 0,
    val description: String = "",
    val deliveryDays: Int = 3,
    val revisions: Int = 1,
    val features: List<String> = emptyList()
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderId: String = "",
    val gigId: String = "",
    val gigTitle: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val packageName: String = "",
    val price: Int = 0,
    val status: String = "pending",
    val requirements: String = "",
    val progress: Int = 0,
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
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
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
    val rating: Float = 5f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)
    @TypeConverter
    fun toStringList(value: String): List<String> = gson.fromJson(value, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun fromPackageList(value: List<GigPackage>): String = gson.toJson(value)
    @TypeConverter
    fun toPackageList(value: String): List<GigPackage> = gson.fromJson(value, object : TypeToken<List<GigPackage>>() {}.type)

    @TypeConverter
    fun fromStringMap(value: Map<String, String>): String = gson.toJson(value)
    @TypeConverter
    fun toStringMap(value: String): Map<String, String> = gson.fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
}
