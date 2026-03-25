package com.hustl.app.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hustl.app.adapters.ChatListAdapter
import com.hustl.app.data.repository.ChatRepository
import com.hustl.app.databinding.FragmentInboxBinding

class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private val chatRepo = ChatRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chats = chatRepo.getSampleChats()
        val adapter = ChatListAdapter(chats) { chat ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("chat_id", chat.chatId)
            val otherName = chat.participantNames.values.firstOrNull { it != "Arjun M." } ?: "Seller"
            intent.putExtra("other_name", otherName)
            startActivity(intent)
        }

        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
