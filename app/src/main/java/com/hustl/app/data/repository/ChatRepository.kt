package com.hustl.app.data.repository

import android.content.Context
import com.hustl.app.data.local.AppDatabase
import com.hustl.app.data.model.Chat
import com.hustl.app.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart

class ChatRepository(context: Context) {
    private val chatDao = AppDatabase.getDatabase(context).chatDao()
    private val messageDao = AppDatabase.getDatabase(context).messageDao()

    fun getMyChats(userId: String): Flow<List<Chat>> {
        return chatDao.getAllChats().onStart {
            val current = chatDao.getAllChats().first()
            if (current.isEmpty()) {
                chatDao.insertChats(getSampleChats())
            }
        }
    }

    fun getMessages(chatId: String): Flow<List<Message>> {
        return messageDao.getMessagesForChat(chatId).onStart {
            val current = messageDao.getMessagesForChat(chatId).first()
            if (current.isEmpty()) {
                getSampleMessages(chatId).forEach { messageDao.insertMessage(it) }
            }
        }
    }

    suspend fun sendMessage(chatId: String, message: Message): Result<Unit> {
        return try {
            val messageId = "msg_${System.currentTimeMillis()}"
            val finalMessage = message.copy(messageId = messageId, chatId = chatId)
            messageDao.insertMessage(finalMessage)
            
            val chat = chatDao.getChatById(chatId)
            chat?.let {
                val updatedChat = it.copy(
                    lastMessage = finalMessage.text,
                    lastMessageTime = finalMessage.timestamp
                )
                chatDao.updateChat(updatedChat)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getSampleChats(): List<Chat> = listOf(
        Chat(chatId = "chat1", participants = listOf("user1", "seller1"), participantNames = mapOf("user1" to "Arjun M.", "seller1" to "Priya S."), gigTitle = "Logo Design", lastMessage = "I've started on the logo, will send first draft soon!", lastMessageTime = System.currentTimeMillis() - 3600000, unreadCount = 1),
        Chat(chatId = "chat2", participants = listOf("user1", "seller3"), participantNames = mapOf("user1" to "Arjun M.", "seller3" to "Ananya K."), gigTitle = "Blog Article", lastMessage = "Article delivered! Please check and let me know.", lastMessageTime = System.currentTimeMillis() - 86400000, unreadCount = 0),
        Chat(chatId = "chat3", participants = listOf("user1", "seller5"), participantNames = mapOf("user1" to "Arjun M.", "seller5" to "Sana T."), gigTitle = "YouTube Edit", lastMessage = "The final cut is ready for review 🎬", lastMessageTime = System.currentTimeMillis() - 259200000, unreadCount = 0)
    )

    private fun getSampleMessages(chatId: String): List<Message> = when (chatId) {
        "chat1" -> listOf(
            Message(messageId = "m1", chatId = chatId, senderId = "seller1", senderName = "Priya S.", text = "Hey! Just confirmed your order. Excited to work on your logo 🎨", timestamp = System.currentTimeMillis() - 7200000),
            Message(messageId = "m2", chatId = chatId, senderId = "user1", senderName = "Arjun M.", text = "Amazing! Can't wait to see your concepts.", timestamp = System.currentTimeMillis() - 6900000),
            Message(messageId = "m5", chatId = chatId, senderId = "seller1", senderName = "Priya S.", text = "I've started on the logo, will send first draft soon!", timestamp = System.currentTimeMillis() - 3600000)
        )
        else -> emptyList()
    }
}
