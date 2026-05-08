package com.hustl.app.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hustl.app.data.model.Chat
import com.hustl.app.data.model.Message
import com.hustl.app.data.model.toFirestoreMap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val chatsCol = db.collection("chats")

    // ─── Chat List ────────────────────────────────────────────────────────────

    fun getMyChats(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = chatsCol
            .whereArrayContains("participants", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val chats = snapshot?.documents
                    ?.mapNotNull { it.toChat() }
                    ?.sortedByDescending { it.lastMessageTime }
                    ?: emptyList()
                trySend(chats)
            }
        awaitClose { listener.remove() }
    }

    // ─── Messages ─────────────────────────────────────────────────────────────

    fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = chatsCol.document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { trySend(emptyList()); return@addSnapshotListener }
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    Message(
                        messageId = doc.id,
                        chatId = chatId,
                        senderId = doc.getString("senderId") ?: "",
                        senderName = doc.getString("senderName") ?: "",
                        text = doc.getString("text") ?: "",
                        attachmentUrl = doc.getString("attachmentUrl") ?: "",
                        type = doc.getString("type") ?: "text",
                        seen = doc.getBoolean("seen") ?: false,
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                } ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    // ─── Send Message ─────────────────────────────────────────────────────────

    suspend fun sendMessage(chatId: String, message: Message): Result<Unit> {
        return try {
            val messagesCol = chatsCol.document(chatId).collection("messages")
            val docRef = messagesCol.document()
            val finalMessage = message.copy(messageId = docRef.id, chatId = chatId)

            db.runBatch { batch ->
                // Write the message
                batch.set(docRef, finalMessage.toFirestoreMap())
                // Update the parent chat document
                batch.update(
                    chatsCol.document(chatId),
                    mapOf(
                        "lastMessage" to finalMessage.text.ifEmpty { "📎 Attachment" },
                        "lastMessageTime" to finalMessage.timestamp,
                        "updatedAt" to finalMessage.timestamp
                    )
                )
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Get or Create Chat ───────────────────────────────────────────────────

    suspend fun getOrCreateChat(
        currentUserId: String,
        currentUserName: String,
        otherUserId: String,
        otherUserName: String,
        gigId: String,
        gigTitle: String
    ): Result<String> {
        return try {
            // Check if a chat already exists between these two users for this gig
            val existing = chatsCol
                .whereArrayContains("participants", currentUserId)
                .whereEqualTo("gigId", gigId)
                .get()
                .await()

            val existingChat = existing.documents
                .firstOrNull { doc ->
                    val participants = doc.get("participants") as? List<*>
                    participants?.contains(otherUserId) == true
                }

            if (existingChat != null) {
                return Result.success(existingChat.id)
            }

            // Create a new chat
            val docRef = chatsCol.document()
            val chat = mapOf(
                "chatId" to docRef.id,
                "participants" to listOf(currentUserId, otherUserId),
                "participantNames" to mapOf(
                    currentUserId to currentUserName,
                    otherUserId to otherUserName
                ),
                "gigId" to gigId,
                "gigTitle" to gigTitle,
                "lastMessage" to "",
                "lastMessageTime" to System.currentTimeMillis(),
                "unreadCounts" to mapOf(currentUserId to 0, otherUserId to 0),
                "createdAt" to System.currentTimeMillis()
            )
            docRef.set(chat).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Read Receipts ────────────────────────────────────────────────────────

    suspend fun markMessagesAsSeen(chatId: String, currentUserId: String) {
        try {
            val unreadMessages = chatsCol.document(chatId)
                .collection("messages")
                .whereEqualTo("seen", false)
                .whereNotEqualTo("senderId", currentUserId)
                .get()
                .await()

            if (unreadMessages.isEmpty) return
            db.runBatch { batch ->
                unreadMessages.documents.forEach { batch.update(it.reference, "seen", true) }
            }.await()
        } catch (_: Exception) {}
    }
}

// ─── DocumentSnapshot extension ───────────────────────────────────────────────

@Suppress("UNCHECKED_CAST")
private fun com.google.firebase.firestore.DocumentSnapshot.toChat(): Chat? {
    val id = this.id.ifEmpty { return null }
    return try {
        val participants = get("participants") as? List<String> ?: emptyList()
        val participantNames = get("participantNames") as? Map<String, String> ?: emptyMap()
        Chat(
            chatId = id,
            participants = participants,
            participantNames = participantNames,
            gigId = getString("gigId") ?: "",
            gigTitle = getString("gigTitle") ?: "",
            lastMessage = getString("lastMessage") ?: "",
            lastMessageTime = getLong("lastMessageTime") ?: 0L,
            unreadCount = (getLong("unreadCount") ?: 0L).toInt()
        )
    } catch (_: Exception) { null }
}
