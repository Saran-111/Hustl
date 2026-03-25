package com.hustl.app.ui.chat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hustl.app.adapters.MessageAdapter
import com.hustl.app.data.model.Message
import com.hustl.app.data.repository.ChatRepository
import com.hustl.app.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatRepo = ChatRepository()
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private var chatId = ""
    private var otherName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatId = intent.getStringExtra("chat_id") ?: "chat1"
        otherName = intent.getStringExtra("other_name") ?: "Seller"

        binding.tvChatName.text = otherName
        binding.tvChatInitials.text = otherName.split(" ").take(2).joinToString("") { it.first().toString() }

        binding.btnBack.setOnClickListener { finish() }

        messageAdapter = MessageAdapter(messages)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).also { it.stackFromEnd = true }
            adapter = messageAdapter
        }

        loadMessages()
        setupInput()
    }

    private fun loadMessages() {
        val loaded = chatRepo.getSampleMessages(chatId).toMutableList()
        messages.addAll(loaded)
        messageAdapter.notifyDataSetChanged()
        scrollToBottom()
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

        val msg = Message(
            chatId = chatId,
            senderId = "user1",
            senderName = "You",
            text = text,
            timestamp = System.currentTimeMillis()
        )
        messages.add(msg)
        messageAdapter.notifyItemInserted(messages.size - 1)
        binding.etMessage.setText("")
        scrollToBottom()

        // Simulate reply after 1.5 seconds
        val replies = listOf("Got it! 👍", "Thanks for letting me know.", "I'll get right on that!", "Sounds great! 🎉", "On it! Will update you soon.")
        binding.rvMessages.postDelayed({
            val reply = Message(
                chatId = chatId,
                senderId = "seller",
                senderName = otherName,
                text = replies.random(),
                timestamp = System.currentTimeMillis()
            )
            messages.add(reply)
            messageAdapter.notifyItemInserted(messages.size - 1)
            scrollToBottom()
        }, 1500)
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) binding.rvMessages.smoothScrollToPosition(messages.size - 1)
    }
}
