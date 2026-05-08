package com.hustl.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hustl.app.adapters.NotificationAdapter
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.NotificationRepository
import com.hustl.app.databinding.ActivityNotificationsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var notifyRepo: NotificationRepository
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notifyRepo = NotificationRepository()
        authRepo = AuthRepository(this)

        binding.btnBack.setOnClickListener { finish() }

        loadNotifications()
    }

    private fun loadNotifications() {
        val currentUser = authRepo.currentUser ?: return
        lifecycleScope.launch {
            notifyRepo.getAllNotifications(currentUser.userId).collectLatest { notifications ->
                if (notifications.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvNotifications.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvNotifications.visibility = View.VISIBLE
                    binding.rvNotifications.apply {
                        layoutManager = LinearLayoutManager(this@NotificationsActivity)
                        adapter = NotificationAdapter(notifications.sortedByDescending { it.timestamp })
                    }
                }
            }
        }
    }
}
