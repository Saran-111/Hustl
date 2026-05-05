package com.hustl.app.ui.chat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
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
    private val messages = mutableListOf<Message>()
    private var chatId = ""
    private var otherName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRepo = ChatRepository(this)
        authRepo = AuthRepository(this)

        chatId = intent.getStringExtra("chat_id") ?: "chat1"
        otherName = intent.getStringExtra("other_name") ?: "Seller"

        binding.tvChatName.text = otherName
        binding.tvChatInitials.text = if (otherName.isNotEmpty()) {
            otherName.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
        } else "S"

        binding.btnBack.setOnClickListener { finish() }

        val currentUserId = authRepo.currentUser?.userId ?: "user1"
        messageAdapter = MessageAdapter(currentUserId)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).also { it.stackFromEnd = true }
            adapter = messageAdapter
        }

        observeMessages()
        setupInput()
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            chatRepo.getMessages(chatId).collectLatest { loadedMessages ->
                messages.clear()
                messages.addAll(loadedMessages)
                messageAdapter.submitList(loadedMessages)
                scrollToBottom()
            }
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
        if (text.isEmpty()) return

        val currentUser = authRepo.currentUser
        val msg = Message(
            chatId = chatId,
            senderId = currentUser?.userId ?: "user1",
            senderName = currentUser?.name ?: "You",
            text = text,
            timestamp = System.currentTimeMillis()
        )
        
        lifecycleScope.launch {
            chatRepo.sendMessage(chatId, msg)
            binding.etMessage.setText("")
            
            if (messages.count { it.senderId == (currentUser?.userId ?: "user1") } == 1) {
                simulateReply()
            }
        }
    }

    private fun simulateReply() {
        binding.rvMessages.postDelayed({
            val replies = listOf("Got it! 👍", "Thanks for letting me know.", "I'll get right on that!", "Sounds great! 🎉", "On it! Will update you soon.")
            val reply = Message(
                chatId = chatId,
                senderId = "seller",
                senderName = otherName,
                text = replies.random(),
                timestamp = System.currentTimeMillis()
            )
            lifecycleScope.launch {
                chatRepo.sendMessage(chatId, reply)
            }
        }, 1500)
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            binding.rvMessages.post {
                binding.rvMessages.smoothScrollToPosition(messages.size - 1)
            }
        }
    }
}
