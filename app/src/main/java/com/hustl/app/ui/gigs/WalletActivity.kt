package com.hustl.app.ui.gigs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.databinding.ActivityWalletBinding
import kotlinx.coroutines.launch

class WalletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalletBinding
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepo = AuthRepository(this)
        
        loadBalance()

        binding.btnBack.setOnClickListener { finish() }

        binding.btnRecharge.setOnClickListener {
            val amountStr = binding.etRechargeAmount.text.toString().trim()
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toInt()
            if (amount <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            rechargeWallet(amount)
        }
    }

    private fun loadBalance() {
        val currentUser = authRepo.currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                val user = authRepo.getUserProfile(currentUser.userId)
                binding.tvWalletBalance.text = "₹${"%,d".format(user.walletBalance)}"
            }
        }
    }

    private fun rechargeWallet(amount: Int) {
        binding.btnRecharge.isEnabled = false
        binding.btnRecharge.text = "Processing Recharge..."

        lifecycleScope.launch {
            // Simulate payment gateway delay
            kotlinx.coroutines.delay(2000)

            val success = authRepo.updateWalletBalance(amount)
            if (success) {
                Toast.makeText(this@WalletActivity, "₹$amount added to wallet! 🎉", Toast.LENGTH_LONG).show()
                loadBalance()
                binding.etRechargeAmount.setText("")
            } else {
                Toast.makeText(this@WalletActivity, "Recharge Failed", Toast.LENGTH_SHORT).show()
            }
            binding.btnRecharge.isEnabled = true
            binding.btnRecharge.text = "Add Money"
        }
    }
}
