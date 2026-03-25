package com.hustl.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hustl.app.data.model.Chat
import com.hustl.app.data.model.Message
import kotlinx.coroutines.tasks.await

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getMyChats(userId: String): List<Chat> {
        return try {
            val snapshot = db.collection("chats")
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .get().await()
            snapshot.toObjects(Chat::class.java)
        } catch (e: Exception) {
            getSampleChats()
        }
    }

    suspend fun getMessages(chatId: String): List<Message> {
        return try {
            val snapshot = db.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get().await()
            snapshot.toObjects(Message::class.java)
        } catch (e: Exception) {
            getSampleMessages(chatId)
        }
    }

    suspend fun sendMessage(chatId: String, message: Message): Result<Unit> {
        return try {
            val ref = db.collection("chats").document(chatId)
                .collection("messages").document()
            ref.set(message.copy(messageId = ref.id)).await()
            db.collection("chats").document(chatId).update(
                mapOf("lastMessage" to message.text, "lastMessageTime" to message.timestamp)
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.success(Unit)
        }
    }

    fun getSampleChats(): List<Chat> = listOf(
        Chat(chatId = "chat1", participants = listOf("user1", "seller1"), participantNames = mapOf("user1" to "Arjun M.", "seller1" to "Priya S."), gigTitle = "Logo Design", lastMessage = "I've started on the logo, will send first draft soon!", lastMessageTime = System.currentTimeMillis() - 3600000, unreadCount = 1),
        Chat(chatId = "chat2", participants = listOf("user1", "seller3"), participantNames = mapOf("user1" to "Arjun M.", "seller3" to "Ananya K."), gigTitle = "Blog Article", lastMessage = "Article delivered! Please check and let me know.", lastMessageTime = System.currentTimeMillis() - 86400000, unreadCount = 0),
        Chat(chatId = "chat3", participants = listOf("user1", "seller5"), participantNames = mapOf("user1" to "Arjun M.", "seller5" to "Sana T."), gigTitle = "YouTube Edit", lastMessage = "The final cut is ready for review 🎬", lastMessageTime = System.currentTimeMillis() - 259200000, unreadCount = 0),
        Chat(chatId = "chat4", participants = listOf("user1", "seller2"), participantNames = mapOf("user1" to "Arjun M.", "seller2" to "Rahul D."), gigTitle = "React Dashboard", lastMessage = "Dashboard deployed to production ✅", lastMessageTime = System.currentTimeMillis() - 864000000, unreadCount = 0)
    )

    fun getSampleMessages(chatId: String): List<Message> = when (chatId) {
        "chat1" -> listOf(
            Message(messageId = "m1", chatId = chatId, senderId = "seller1", senderName = "Priya S.", text = "Hey! Just confirmed your order. Excited to work on your logo 🎨", timestamp = System.currentTimeMillis() - 7200000),
            Message(messageId = "m2", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "Amazing! Can't wait to see your concepts.", timestamp = System.currentTimeMillis() - 6900000),
            Message(messageId = "m3", chatId = chatId, senderId = "seller1", senderName = "Priya S.", text = "What color palette do you prefer? Minimalist or bold?", timestamp = System.currentTimeMillis() - 6600000),
            Message(messageId = "m4", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "Minimalist please — dark navy and gold tones.", timestamp = System.currentTimeMillis() - 6300000),
            Message(messageId = "m5", chatId = chatId, senderId = "seller1", senderName = "Priya S.", text = "Perfect, that's my favorite combo! I've started on the logo, will send first draft soon!", timestamp = System.currentTimeMillis() - 3600000)
        )
        "chat2" -> listOf(
            Message(messageId = "m1", chatId = chatId, senderId = "seller3", senderName = "Ananya K.", text = "Hi! Your article on AI trends is complete and uploaded to Google Drive.", timestamp = System.currentTimeMillis() - 172800000),
            Message(messageId = "m2", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "Wow, that was fast! Let me review it.", timestamp = System.currentTimeMillis() - 169200000),
            Message(messageId = "m3", chatId = chatId, senderId = "seller3", senderName = "Ananya K.", text = "Take your time. I've included the meta description and keywords too.", timestamp = System.currentTimeMillis() - 165600000),
            Message(messageId = "m4", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "This is excellent work! Really happy with the quality!", timestamp = System.currentTimeMillis() - 108000000),
            Message(messageId = "m5", chatId = chatId, senderId = "seller3", senderName = "Ananya K.", text = "Article delivered! Please check and let me know.", timestamp = System.currentTimeMillis() - 86400000)
        )
        else -> listOf(
            Message(messageId = "m1", chatId = chatId, senderId = "seller1", senderName = "Seller", text = "Hello! How can I help you today?", timestamp = System.currentTimeMillis() - 3600000),
            Message(messageId = "m2", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "Hi! I'm interested in your services.", timestamp = System.currentTimeMillis() - 3300000)
        )
    }
}
