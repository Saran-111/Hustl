package com.hustl.app.ui.gigs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hustl.app.data.model.Order
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.ActivityPaymentBinding
import kotlinx.coroutines.launch
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var orderRepo: OrderRepository
    private lateinit var authRepo: AuthRepository
    private var walletBalance = 0
    private var generatedKey: String? = null

    // Pending order details to use after authentication
    private var pendingOrderData: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderRepo = OrderRepository(this)
        authRepo = AuthRepository(this)

        setupPaymentMethods()
        loadWalletBalance()
        setupWebView()

        val gigId = intent.getStringExtra("gig_id") ?: ""
        val gigTitle = intent.getStringExtra("gig_title") ?: ""
        val sellerName = intent.getStringExtra("seller_name") ?: ""
        val packageName = intent.getStringExtra("package_name") ?: ""
        val price = intent.getIntExtra("price", 0)
        val requirements = intent.getStringExtra("requirements") ?: ""

        binding.tvGigTitle.text = gigTitle
        binding.tvPackageName.text = packageName
        binding.tvAmount.text = "₹${"%,d".format(price)}"

        binding.btnBack.setOnClickListener { finish() }

        binding.btnPay.setOnClickListener {
            if (validateInputs(price)) {
                startAuthentication(gigId, gigTitle, sellerName, packageName, price, requirements)
            }
        }
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(), "Android")
            webViewClient = WebViewClient()
        }
    }

    private fun setupPaymentMethods() {
        binding.rgPayment.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbUpi.id -> {
                    binding.layoutUpi.visibility = View.VISIBLE
                    binding.layoutCard.visibility = View.GONE
                }
                binding.rbCard.id -> {
                    binding.layoutUpi.visibility = View.GONE
                    binding.layoutCard.visibility = View.VISIBLE
                }
                binding.rbWallet.id -> {
                    binding.layoutUpi.visibility = View.GONE
                    binding.layoutCard.visibility = View.GONE
                }
            }
        }
    }

    private fun loadWalletBalance() {
        val currentUser = authRepo.currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                val user = authRepo.getUserProfile(currentUser.userId)
                walletBalance = user.walletBalance
                binding.rbWallet.text = "Hustl Wallet (Balance: ₹${"%,d".format(walletBalance)})"
            }
        }
    }

    private fun validateInputs(price: Int): Boolean {
        return when (binding.rgPayment.checkedRadioButtonId) {
            binding.rbUpi.id -> {
                val upi = binding.etUpiId.text.toString().trim()
                if (upi.isEmpty() || !upi.contains("@")) {
                    Toast.makeText(this, "Enter a valid UPI ID", Toast.LENGTH_SHORT).show()
                    false
                } else true
            }
            binding.rbCard.id -> {
                val card = binding.etCardNumber.text.toString().trim()
                val expiry = binding.etExpiry.text.toString().trim()
                val cvv = binding.etCvv.text.toString().trim()
                if (card.length < 16) {
                    Toast.makeText(this, "Enter valid 16-digit Card Number", Toast.LENGTH_SHORT).show()
                    false
                } else if (expiry.isEmpty()) {
                    Toast.makeText(this, "Enter Expiry Date", Toast.LENGTH_SHORT).show()
                    false
                } else if (cvv.length < 3) {
                    Toast.makeText(this, "Enter 3-digit CVV", Toast.LENGTH_SHORT).show()
                    false
                } else true
            }
            binding.rbWallet.id -> {
                if (walletBalance < price) {
                    Toast.makeText(this, "Insufficient Wallet Balance. Please recharge.", Toast.LENGTH_LONG).show()
                    false
                } else true
            }
            else -> false
        }
    }

    private fun startAuthentication(
        gigId: String,
        gigTitle: String,
        sellerName: String,
        packageName: String,
        price: Int,
        requirements: String
    ) {
        // Store order details for later
        pendingOrderData = Bundle().apply {
            putString("gig_id", gigId)
            putString("gig_title", gigTitle)
            putString("seller_name", sellerName)
            putString("package_name", packageName)
            putInt("price", price)
            putString("requirements", requirements)
        }

        // Generate a random encrypted-style key
        generatedKey = UUID.randomUUID().toString().substring(0, 8).uppercase()

        // Show the WebView overlay
        binding.authOverlay.visibility = View.VISIBLE
        binding.webView.loadUrl("file:///android_asset/payment_auth.html?auth_key=$generatedKey")
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onAuthFinished(responseKey: String) {
            runOnUiThread {
                binding.authOverlay.visibility = View.GONE
                verifyAndProcess(responseKey)
            }
        }
    }

    private fun verifyAndProcess(responseKey: String) {
        val expectedResponse = generatedKey?.reversed() + "_VERIFIED"
        
        if (responseKey == expectedResponse) {
            pendingOrderData?.let { data ->
                processPayment(
                    data.getString("gig_id") ?: "",
                    data.getString("gig_title") ?: "",
                    data.getString("seller_name") ?: "",
                    data.getString("package_name") ?: "",
                    data.getInt("price"),
                    data.getString("requirements") ?: ""
                )
            }
        } else {
            Toast.makeText(this, "Authentication Failed. Payment Security Breach.", Toast.LENGTH_LONG).show()
            binding.btnPay.isEnabled = true
            binding.btnPay.text = "Pay Now"
        }
    }

    private fun processPayment(
        gigId: String,
        gigTitle: String,
        sellerName: String,
        packageName: String,
        price: Int,
        requirements: String
    ) {
        binding.btnPay.isEnabled = false
        binding.btnPay.text = "Authorizing..."

        lifecycleScope.launch {
            kotlinx.coroutines.delay(1000)

            val currentUser = authRepo.currentUser
            if (currentUser == null) {
                Toast.makeText(this@PaymentActivity, "User session expired", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Deduct from wallet if selected
            if (binding.rgPayment.checkedRadioButtonId == binding.rbWallet.id) {
                val success = authRepo.deductWalletBalance(price)
                if (!success) {
                    Toast.makeText(this@PaymentActivity, "Wallet deduction failed", Toast.LENGTH_SHORT).show()
                    binding.btnPay.isEnabled = true
                    binding.btnPay.text = "Pay Now"
                    return@launch
                }
            }

            val order = Order(
                gigId = gigId,
                gigTitle = gigTitle,
                buyerId = currentUser.userId,
                sellerId = "seller1",
                sellerName = sellerName,
                packageName = packageName,
                price = price,
                status = "pending",
                requirements = requirements,
                progress = 0,
                createdAt = System.currentTimeMillis()
            )

            val result = orderRepo.placeOrder(order)
            result.fold(
                onSuccess = { orderId ->
                    Toast.makeText(this@PaymentActivity, "Payment Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PaymentActivity, OrderSuccessActivity::class.java)
                    intent.putExtra("order_id", orderId)
                    intent.putExtra("price", price)
                    startActivity(intent)
                    finish()
                },
                onFailure = {
                    Toast.makeText(this@PaymentActivity, "Payment Failed", Toast.LENGTH_SHORT).show()
                    binding.btnPay.isEnabled = true
                    binding.btnPay.text = "Pay Now"
                }
            )
        }
    }
}
