package com.hustl.app.ui.gigs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hustl.app.adapters.PackageAdapter
import com.hustl.app.adapters.ReviewAdapter
import com.hustl.app.data.model.GigPackage
import com.hustl.app.data.model.Order
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.ActivityGigDetailBinding
import com.hustl.app.databinding.BottomSheetOrderBinding
import com.hustl.app.ui.chat.ChatActivity
import kotlinx.coroutines.launch

class GigDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGigDetailBinding
    private val gigRepo = GigRepository()
    private val orderRepo = OrderRepository()
    private var selectedPackageIdx = 0
    private var gigId = ""
    private var isFaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGigDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gigId = intent.getStringExtra("gig_id") ?: ""
        loadGig()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnFavorite.setOnClickListener { toggleFav() }
        binding.btnContact.setOnClickListener { openChat() }
    }

    private fun loadGig() {
        lifecycleScope.launch {
            val gig = gigRepo.getSampleGigs().find { it.gigId == gigId }
                ?: gigRepo.getSampleGigs().first()

            // Hero emoji/color
            val emojis = mapOf("Design" to "🎨", "Development" to "💻", "Writing" to "✍️",
                "Marketing" to "📣", "Video" to "🎬", "Music" to "🎵")
            binding.tvGigEmoji.text = emojis[gig.category] ?: "⚡"

            // Note: FrameLayout parent doesn't have an ID, using a workaround or just skipping color if not needed
            // For now, let's just use the tvGigEmoji's parent if possible or ignore the background color change
            // to avoid build errors if the view hierarchy isn't perfectly mapped.

            binding.tvCategory.text = gig.category
            binding.tvGigTitle.text = gig.title
            binding.tvSellerName.text = gig.sellerName
            binding.tvSellerTitle.text = "${gig.category} Expert"
            binding.tvRating.text = "★ ${gig.rating}"
            binding.tvReviewCount.text = "${gig.reviewCount} reviews"
            binding.tvDescription.text = gig.description
            binding.tvSellerInitials.text = gig.sellerName.split(" ").take(2).joinToString("") { it.first().toString() }

            // Packages
            val pkgAdapter = PackageAdapter(gig.packages) { idx ->
                selectedPackageIdx = idx
            }
            binding.rvPackages.apply {
                layoutManager = LinearLayoutManager(this@GigDetailActivity)
                adapter = pkgAdapter
                isNestedScrollingEnabled = false
            }

            // Reviews
            val reviews = gigRepo.getSampleReviews(gigId)
            val reviewAdapter = ReviewAdapter(reviews)
            binding.rvReviews.apply {
                layoutManager = LinearLayoutManager(this@GigDetailActivity)
                adapter = reviewAdapter
                isNestedScrollingEnabled = false
            }
            binding.tvReviewsHeader.text = "Reviews (${reviews.size})"

            // Starting price
            val minPrice = gig.packages.minOfOrNull { it.price } ?: 0
            binding.tvStartingPrice.text = "From ₹${"%,d".format(minPrice)}"

            binding.btnOrderNow.setOnClickListener { showOrderSheet(gig.packages) }
        }
    }

    private fun showOrderSheet(packages: List<GigPackage>) {
        val pkg = packages.getOrNull(selectedPackageIdx) ?: packages.first()
        val dialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetOrderBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        sheetBinding.tvPackageName.text = "${pkg.name} Package"
        sheetBinding.tvPackagePrice.text = "₹${"%,d".format(pkg.price)}"
        sheetBinding.tvPackageDesc.text = pkg.description
        sheetBinding.tvDelivery.text = "Delivered in ${pkg.deliveryDays} days"

        sheetBinding.btnConfirmOrder.setOnClickListener {
            val requirements = sheetBinding.etRequirements.text.toString()
            placeOrder(pkg, requirements)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun placeOrder(pkg: GigPackage, requirements: String) {
        val order = Order(
            gigId = gigId,
            gigTitle = binding.tvGigTitle.text.toString(),
            buyerId = "user1",
            sellerId = "seller1",
            sellerName = binding.tvSellerName.text.toString(),
            packageName = pkg.name,
            price = pkg.price,
            status = "pending",
            requirements = requirements
        )

        lifecycleScope.launch {
            val result = orderRepo.placeOrder(order)
            result.fold(
                onSuccess = { orderId ->
                    val intent = Intent(this@GigDetailActivity, OrderSuccessActivity::class.java)
                    intent.putExtra("order_id", "HU-$orderId")
                    intent.putExtra("price", pkg.price)
                    startActivity(intent)
                },
                onFailure = {
                    Toast.makeText(this@GigDetailActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            )
        }
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
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chat_id", "chat1")
        intent.putExtra("other_name", binding.tvSellerName.text.toString())
        startActivity(intent)
    }
}
