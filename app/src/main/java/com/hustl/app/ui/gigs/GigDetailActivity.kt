package com.hustl.app.ui.gigs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hustl.app.adapters.ReviewAdapter
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.ActivityGigDetailBinding
import com.hustl.app.databinding.BottomSheetOrderBinding
import com.hustl.app.ui.chat.ChatActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GigDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGigDetailBinding
    private lateinit var gigRepo: GigRepository
    private lateinit var orderRepo: OrderRepository
    private lateinit var authRepo: AuthRepository
    private var selectedPackageIdx = 0
    private var gigId = ""
    private var isFaved = false
    private var currentGigTitle = ""
    private var currentSellerName = ""
    private var currentSellerId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGigDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gigRepo = GigRepository()
        orderRepo = OrderRepository(this)
        authRepo = AuthRepository(this)

        gigId = intent.getStringExtra("gig_id") ?: ""
        loadGig()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnFavorite.setOnClickListener { toggleFav() }
        binding.btnContact.setOnClickListener { openChat() }
    }

    private fun loadGig() {
        lifecycleScope.launch {
            val gig = gigRepo.getGigById(gigId) ?: gigRepo.getAllGigs().first().firstOrNull() ?: return@launch
            
            currentGigTitle = gig.title
            currentSellerName = gig.sellerName
            currentSellerId = gig.sellerId

            val emojis = mapOf("Design" to "🎨", "Development" to "💻", "Writing" to "✍️",
                "Marketing" to "📣", "Video" to "🎬", "Music" to "🎵")
            binding.tvGigEmoji.text = emojis[gig.category] ?: "⚡"

            binding.tvCategory.text = gig.category
            binding.tvGigTitle.text = gig.title
            binding.tvSellerName.text = gig.sellerName
            binding.tvSellerTitle.text = "${gig.category} Expert"
            binding.tvRating.text = "★ ${gig.rating}"
            binding.tvReviewCount.text = "${gig.reviewCount} reviews"
            binding.tvDescription.text = gig.description
            binding.tvSellerInitials.text = if (gig.sellerName.isNotEmpty()) {
                gig.sellerName.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
            } else "S"

            val openProfile = View.OnClickListener {
                val intent = Intent(this@GigDetailActivity, com.hustl.app.ui.profile.HustlrProfileActivity::class.java)
                intent.putExtra("hustlr_id", currentSellerId)
                startActivity(intent)
            }
            binding.tvSellerName.setOnClickListener(openProfile)
            binding.tvSellerInitials.setOnClickListener(openProfile)

            // Load reviews from Firestore (real-time)
            lifecycleScope.launch {
                gigRepo.getReviewsForGig(gigId).collectLatest { reviews ->
                    val reviewAdapter = ReviewAdapter(reviews)
                    binding.rvReviews.apply {
                        layoutManager = LinearLayoutManager(this@GigDetailActivity)
                        adapter = reviewAdapter
                        isNestedScrollingEnabled = false
                    }
                    binding.tvReviewsHeader.text = "Reviews (${reviews.size})"
                }
            }

            binding.tvPriceRange.text = "₹${gig.minPrice} - ₹${gig.maxPrice}"
            binding.tvDelivery.text = "🕒 ${gig.deliveryDays} Days Delivery"
            binding.tvRevisions.text = "🔄 ${if (gig.revisions == -1) "Unlimited" else "${gig.revisions}"} Revisions"

            val currentUser = authRepo.currentUser
            if (currentUser?.userId == currentSellerId) {
                binding.btnOrderNow.visibility = View.GONE
            } else {
                binding.btnOrderNow.visibility = View.VISIBLE
                binding.btnOrderNow.setOnClickListener { showOrderSheet(gig) }
            }
        }
    }

    private fun showOrderSheet(gig: com.hustl.app.data.model.Gig) {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetOrderBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        sheetBinding.tvDelivery.text = "Delivered in ${gig.deliveryDays} days"
        sheetBinding.tvRevisions.text = "${if (gig.revisions == -1) "Unlimited" else "${gig.revisions}"} Revisions"
        sheetBinding.etFinalPrice.hint = "₹${gig.minPrice} - ₹${gig.maxPrice}"

        sheetBinding.btnConfirmOrder.setOnClickListener {
            val requirements = sheetBinding.etRequirements.text.toString()
            val finalPriceStr = sheetBinding.etFinalPrice.text.toString()
            
            if (finalPriceStr.isEmpty()) {
                sheetBinding.etFinalPrice.error = "Please enter an agreed price"
                return@setOnClickListener
            }
            
            val price = finalPriceStr.toIntOrNull() ?: 0
            if (price < gig.minPrice || price > gig.maxPrice) {
                sheetBinding.etFinalPrice.error = "Price must be within range"
                return@setOnClickListener
            }
            
            // Navigate to Payment module
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("gig_id", gigId)
                putExtra("gig_title", currentGigTitle)
                putExtra("seller_name", currentSellerName)
                putExtra("seller_id", currentSellerId)
                putExtra("package_name", "Custom Order")
                putExtra("price", price)
                putExtra("requirements", requirements)
            }
            startActivity(intent)
            dialog.dismiss()
        }

        sheetBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun toggleFav() {
        isFaved = !isFaved
        binding.btnFavorite.setImageResource(
            if (isFaved) com.hustl.app.R.drawable.ic_heart_filled
            else com.hustl.app.R.drawable.ic_heart_outline
        )
        Toast.makeText(this, if (isFaved) "Saved to favorites!" else "Removed from favorites", Toast.LENGTH_SHORT).show()
    }

    private fun openChat() {
        val currentUser = authRepo.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to contact the Hustlr", Toast.LENGTH_SHORT).show()
            return
        }
        if (currentUser.userId == currentSellerId) {
            Toast.makeText(this, "This is your own gig!", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("other_user_id", currentSellerId)
            putExtra("other_name", currentSellerName)
            putExtra("gig_id", gigId)
            putExtra("gig_title", currentGigTitle)
        }
        startActivity(intent)
    }
}
