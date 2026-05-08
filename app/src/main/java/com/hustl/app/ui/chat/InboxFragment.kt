package com.hustl.app.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hustl.app.adapters.ChatListAdapter
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.ChatRepository
import com.hustl.app.databinding.FragmentInboxBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatRepo: ChatRepository
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatRepo = ChatRepository(requireContext())
        authRepo = AuthRepository(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChats()
    }

    private fun observeChats() {
        val currentUser = authRepo.currentUser
        val currentUserName = currentUser?.name ?: "User"
        viewLifecycleOwner.lifecycleScope.launch {
            chatRepo.getMyChats(currentUser?.userId ?: "user1").collectLatest { chats ->
                val adapter = ChatListAdapter(chats, currentUserName) { chat ->
                    val intent = Intent(requireContext(), ChatActivity::class.java)
                    intent.putExtra("chat_id", chat.chatId)
                    val otherName = chat.participantNames.values.firstOrNull { it != currentUserName } ?: "User"
                    intent.putExtra("other_name", otherName)
                    startActivity(intent)
                }

                binding.rvChats.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    this.adapter = adapter
                }
                
                binding.tvEmpty.visibility = if (chats.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
