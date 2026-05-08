package com.hustl.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.hustl.app.R
import com.hustl.app.adapters.GigAdapter
import com.hustl.app.adapters.ReviewAdapter
import com.hustl.app.data.model.Review
import com.hustl.app.data.model.User
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.data.repository.NotificationRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.ActivityHustlrProfileBinding
import com.hustl.app.ui.gigs.GigDetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HustlrProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHustlrProfileBinding
    private lateinit var authRepo: AuthRepository
    private lateinit var gigRepo: GigRepository
    private lateinit var orderRepo: OrderRepository
    private lateinit var notifyRepo: NotificationRepository

    private var hustlrId = ""
    private var hustlrName = ""

    private lateinit var gigAdapter: GigAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHustlrProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepo = AuthRepository(this)
        gigRepo = GigRepository()
        orderRepo = OrderRepository(this)
        notifyRepo = NotificationRepository()

        hustlrId = intent.getStringExtra("hustlr_id") ?: ""
        if (hustlrId.isEmpty()) {
            Toast.makeText(this, "Invalid Profile", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        loadHustlrProfile()
        loadGigsAndReviews()
        checkIfCanReview()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { finish() }

        gigAdapter = GigAdapter(isHorizontal = false) { gig ->
            val intent = Intent(this, GigDetailActivity::class.java)
            intent.putExtra("gig_id", gig.gigId)
            startActivity(intent)
        }
        binding.rvGigs.apply {
            layoutManager = GridLayoutManager(this@HustlrProfileActivity, 2)
            adapter = gigAdapter
            isNestedScrollingEnabled = false
        }

        reviewAdapter = ReviewAdapter(emptyList())
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@HustlrProfileActivity)
            adapter = reviewAdapter
            isNestedScrollingEnabled = false
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvGigs.visibility = View.VISIBLE
                        binding.rvReviews.visibility = View.GONE
                        binding.tvEmpty.visibility = if (gigAdapter.currentList.isEmpty()) View.VISIBLE else View.GONE
                    }
                    1 -> {
                        binding.rvGigs.visibility = View.GONE
                        binding.rvReviews.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = if (reviewAdapter.itemCount == 0) View.VISIBLE else View.GONE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.btnLeaveReview.setOnClickListener {
            showReviewSheet()
        }
    }

    private fun loadHustlrProfile() {
        lifecycleScope.launch {
            val user = authRepo.getUserProfile(hustlrId)
            hustlrName = user.name
            binding.tvName.text = user.name
            binding.tvInitials.text = user.name.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().toString() }
            binding.tvLocation.text = user.location.ifEmpty { "Location not specified" }
            binding.tvRating.text = String.format("⭐ %.1f (%d reviews)", user.rating, user.reviewCount)

            val orders = orderRepo.getMyOrders(hustlrId).first()
            val completed = orders.count { it.sellerId == hustlrId && it.status == "completed" }
            binding.tvTotalOrders.text = completed.toString()
        }
    }

    private fun loadGigsAndReviews() {
        lifecycleScope.launch {
            gigRepo.getGigsByHustlr(hustlrId).collectLatest { gigs ->
                gigAdapter.submitList(gigs)
                if (binding.tabLayout.selectedTabPosition == 0) {
                    binding.tvEmpty.visibility = if (gigs.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        lifecycleScope.launch {
            gigRepo.getReviewsForUser(hustlrId).collectLatest { reviews ->
                // ReviewAdapter uses notifyDataSetChanged, we need to create a new instance or add updateList
                // Using a simple trick: just recreate adapter to submit new list
                reviewAdapter = ReviewAdapter(reviews)
                binding.rvReviews.adapter = reviewAdapter

                if (binding.tabLayout.selectedTabPosition == 1) {
                    binding.tvEmpty.visibility = if (reviews.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun checkIfCanReview() {
        val currentUser = authRepo.currentUser ?: return
        if (currentUser.userId == hustlrId) return // Cannot review oneself

        lifecycleScope.launch {
            val orders = orderRepo.getMyOrders(currentUser.userId).first()
            // Check if there is a completed order from this hustlr that hasn't been rated yet
            val canReview = orders.any { it.sellerId == hustlrId && it.status == "completed" && !it.ratingGiven }
            binding.btnLeaveReview.visibility = if (canReview) View.VISIBLE else View.GONE
        }
    }

    private fun showReviewSheet() {
        val currentUser = authRepo.currentUser ?: return
        val dialog = BottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_review, null)
        dialog.setContentView(sheetView)

        val ratingBar = sheetView.findViewById<android.widget.RatingBar>(R.id.ratingBar)
        val etComment = sheetView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etComment)
        val btnSubmit = sheetView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSubmitReview)

        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating
            val comment = etComment.text.toString().trim()
            if (comment.isEmpty()) {
                etComment.error = "Please write a comment"
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false
            btnSubmit.text = "Submitting..."

            lifecycleScope.launch {
                // Find the first unrated order to tie the review to
                val orders = orderRepo.getMyOrders(currentUser.userId).first()
                val orderToRate = orders.firstOrNull { it.sellerId == hustlrId && it.status == "completed" && !it.ratingGiven }
                
                if (orderToRate != null) {
                    val review = Review(
                        gigId = orderToRate.gigId,
                        orderId = orderToRate.orderId,
                        buyerId = currentUser.userId,
                        buyerName = currentUser.name,
                        reviewedUserId = hustlrId,
                        rating = rating,
                        comment = comment,
                        createdAt = System.currentTimeMillis()
                    )

                    val result = gigRepo.addReview(review)
                    result.fold(
                        onSuccess = {
                            lifecycleScope.launch {
                                orderRepo.markRatingGiven(orderToRate.orderId)
                                notifyRepo.sendNotification(
                                    hustlrId,
                                    "New Review Received! ⭐",
                                    "${currentUser.name} left a $rating star review: $comment",
                                    "review"
                                )
                            }
                            dialog.dismiss()
                            checkIfCanReview() 
                            loadHustlrProfile() 
                        },
                        onFailure = {
                            Toast.makeText(this@HustlrProfileActivity, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                            btnSubmit.isEnabled = true
                            btnSubmit.text = "Submit Review"
                        }
                    )
                } else {
                    dialog.dismiss()
                    checkIfCanReview()
                }
            }
        }

        dialog.show()
    }
}
