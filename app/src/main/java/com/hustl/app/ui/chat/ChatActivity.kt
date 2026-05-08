package com.hustl.app.ui.chat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hustl.app.adapters.MessageAdapter
import com.hustl.app.data.model.Message
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.ChatRepository
import com.hustl.app.databinding.ActivityChatBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatRepo: ChatRepository
    private lateinit var authRepo: AuthRepository
    private lateinit var messageAdapter: MessageAdapter

    private var chatId = ""
    private var otherName = ""
    private var otherUserId = ""
    private var gigId = ""
    private var gigTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRepo = ChatRepository(this)
        authRepo = AuthRepository(this)

        otherName = intent.getStringExtra("other_name") ?: "User"
        otherUserId = intent.getStringExtra("other_user_id") ?: ""
        gigId = intent.getStringExtra("gig_id") ?: ""
        gigTitle = intent.getStringExtra("gig_title") ?: ""

        binding.tvChatName.text = otherName
        binding.tvChatInitials.text = otherName
            .split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().toString() }

        binding.btnBack.setOnClickListener { finish() }

        val currentUser = authRepo.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        messageAdapter = MessageAdapter(currentUser.userId)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).also { it.stackFromEnd = true }
            adapter = messageAdapter
        }

        // If chatId is passed directly, use it; otherwise create/get one
        val incomingChatId = intent.getStringExtra("chat_id") ?: ""
        if (incomingChatId.isNotEmpty()) {
            chatId = incomingChatId
            startObserving()
        } else {
            // Create or fetch chat for this gig + user pair
            lifecycleScope.launch {
                val result = chatRepo.getOrCreateChat(
                    currentUserId = currentUser.userId,
                    currentUserName = currentUser.name,
                    otherUserId = otherUserId,
                    otherUserName = otherName,
                    gigId = gigId,
                    gigTitle = gigTitle
                )
                result.fold(
                    onSuccess = { id ->
                        chatId = id
                        startObserving()
                    },
                    onFailure = {
                        Toast.makeText(this@ChatActivity, "Could not open chat: ${it.message}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                )
            }
        }

        setupInput()
    }

    private fun startObserving() {
        lifecycleScope.launch {
            chatRepo.getMessages(chatId).collectLatest { messages ->
                messageAdapter.submitList(messages)
                if (messages.isNotEmpty()) {
                    binding.rvMessages.post {
                        binding.rvMessages.smoothScrollToPosition(messages.size - 1)
                    }
                }
            }
        }

        // Mark messages as read
        val uid = authRepo.currentUser?.userId ?: return
        lifecycleScope.launch {
            chatRepo.markMessagesAsSeen(chatId, uid)
        }
    }

    private fun setupInput() {
        binding.btnSend.setOnClickListener { sendMessage() }
        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) { sendMessage(); true } else false
        }
    }

    private fun sendMessage() {
        val text = binding.etMessage.text.toString().trim()
        if (text.isEmpty() || chatId.isEmpty()) return

        val currentUser = authRepo.currentUser ?: return
        val msg = Message(
            chatId = chatId,
            senderId = currentUser.userId,
            senderName = currentUser.name,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        binding.etMessage.setText("")
        lifecycleScope.launch {
            chatRepo.sendMessage(chatId, msg)
            val notifyRepo = com.hustl.app.data.repository.NotificationRepository()
            notifyRepo.sendNotification(
                targetUserId = otherUserId,
                title = "New Message from ${currentUser.name}",
                body = text,
                type = "message"
            )
        }
    }

    override fun onStop() {
        super.onStop()
        // Update online status
        val uid = authRepo.currentUser?.userId ?: return
        lifecycleScope.launch {
            authRepo.updateOnlineStatus(uid, false)
        }
    }
}
