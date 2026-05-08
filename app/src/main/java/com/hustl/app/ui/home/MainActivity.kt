package com.hustl.app.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hustl.app.R
import com.hustl.app.databinding.ActivityMainBinding
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.NotificationRepository
import com.hustl.app.utils.NotificationHelper
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var notifyRepo: NotificationRepository
    private lateinit var authRepo: AuthRepository
    private val shownNotifications = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        notificationHelper = NotificationHelper(this)
        notifyRepo = NotificationRepository()
        authRepo = AuthRepository(this)

        requestNotificationPermission()
        startNotificationListener()
    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101
                )
            }
        }
    }

    private fun startNotificationListener() {
        lifecycleScope.launch {
            // Wait for user session if it's not cached yet
            val firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser ?: return@launch
            val user = authRepo.getUserProfile(firebaseUser.uid)
            
            // Ensure we have the latest FCM token
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                lifecycleScope.launch {
                    authRepo.updateFcmToken(user.userId, token)
                }
            }
            
            notifyRepo.listenForNotifications(user.userId).collectLatest { notifications ->
                notifications.forEach { notification ->
                    if (notification.id !in shownNotifications) {
                        shownNotifications.add(notification.id)
                        
                        val channelId = when (notification.type) {
                            "order" -> NotificationHelper.CHANNEL_ORDERS
                            "review" -> NotificationHelper.CHANNEL_REVIEWS
                            else -> NotificationHelper.CHANNEL_ORDERS
                        }
                        notificationHelper.showNotification(
                            notification.title,
                            notification.body,
                            channelId
                        )
                        android.widget.Toast.makeText(this@MainActivity, "New Notification: ${notification.title}", android.widget.Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
