package com.hustl.app.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "buyer", // "buyer" or "seller"
    val bio: String = "",
    val profileImageUrl: String = "",
    val rating: Double = 0.0,
    val totalOrders: Int = 0,
    val location: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class Gig(
    val gigId: String = "",
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
    val name: String = "",       // Basic / Standard / Premium
    val price: Int = 0,
    val description: String = "",
    val deliveryDays: Int = 3,
    val revisions: Int = 1,
    val features: List<String> = emptyList()
)

data class Order(
    val orderId: String = "",
    val gigId: String = "",
    val gigTitle: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val packageName: String = "",
    val price: Int = 0,
    val status: String = "pending", // pending / active / completed / cancelled
    val requirements: String = "",
    val progress: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveryDate: Long = 0L
)

data class Message(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class Chat(
    val chatId: String = "",
    val participants: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val gigId: String = "",
    val gigTitle: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0
)

data class Review(
    val reviewId: String = "",
    val gigId: String = "",
    val orderId: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val rating: Float = 5f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
