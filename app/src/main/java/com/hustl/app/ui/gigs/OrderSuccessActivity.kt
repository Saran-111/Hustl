package com.hustl.app.ui.gigs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hustl.app.databinding.ActivityOrderSuccessBinding
import com.hustl.app.ui.home.MainActivity

class OrderSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_id") ?: "HU-0042"
        val price = intent.getIntExtra("price", 0)

        binding.tvOrderId.text = orderId
        binding.tvOrderPrice.text = "₹${"%,d".format(price)}"

        binding.btnViewOrders.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("open_orders", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
