package com.hustl.app.ui.gigs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hustl.app.data.model.Gig
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.databinding.ActivityPostGigBinding
import kotlinx.coroutines.launch

class PostGigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostGigBinding
    private lateinit var gigRepo: GigRepository
    private lateinit var authRepo: AuthRepository

    private val categories = listOf(
        "Design", "Development", "Writing",
        "Marketing", "Video", "Music", "Education", "Business", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostGigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gigRepo = GigRepository()
        authRepo = AuthRepository(this)

        // Guard: only hustlrs can post gigs
        if (!authRepo.isHustlr()) {
            Toast.makeText(this, "Only Hustlrs can post gigs", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupCategorySpinner()
        setupCharacterCounters()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnPublish.setOnClickListener { publishGig() }
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupCharacterCounters() {
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val count = s?.length ?: 0
                binding.tvTitleCount.text = "$count/80 characters"
                binding.tvTitleCount.setTextColor(
                    if (count > 70) getColor(android.R.color.holo_orange_light)
                    else getColor(android.R.color.darker_gray)
                )
                binding.tvDraftStatus.text = "Draft"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val count = s?.length ?: 0
                binding.tvDescCount.text = "$count/1000 characters"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun publishGig() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val tags = binding.etTags.text.toString()
            .split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        val minPrice = binding.etMinPrice.text.toString().trim().toIntOrNull() ?: 0
        val maxPrice = binding.etMaxPrice.text.toString().trim().toIntOrNull() ?: 0
        val deliveryDays = binding.etDeliveryDays.text.toString().trim().toIntOrNull() ?: 3
        val revisions = binding.etRevisions.text.toString().trim().toIntOrNull() ?: 1

        // Validation
        if (title.isEmpty()) {
            binding.etTitle.error = "Title is required"
            binding.etTitle.requestFocus()
            return
        }
        if (title.length < 20) {
            binding.etTitle.error = "Title must be at least 20 characters"
            binding.etTitle.requestFocus()
            return
        }
        if (description.isEmpty()) {
            binding.etDescription.error = "Description is required"
            binding.etDescription.requestFocus()
            return
        }
        if (description.length < 50) {
            binding.etDescription.error = "Description must be at least 50 characters"
            binding.etDescription.requestFocus()
            return
        }
        if (minPrice <= 0) {
            binding.etMinPrice.error = "Minimum price must be greater than 0"
            binding.etMinPrice.requestFocus()
            return
        }
        if (maxPrice < minPrice) {
            binding.etMaxPrice.error = "Maximum price cannot be less than minimum price"
            binding.etMaxPrice.requestFocus()
            return
        }

        val currentUser = authRepo.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        val gig = Gig(
            sellerId = currentUser.userId,
            sellerName = currentUser.name,
            title = title,
            description = description,
            category = category,
            tags = tags,
            minPrice = minPrice,
            maxPrice = maxPrice,
            deliveryDays = deliveryDays,
            revisions = revisions,
            rating = 0.0,
            reviewCount = 0,
            isActive = true
        )

        binding.btnPublish.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        binding.tvDraftStatus.text = "Publishing..."

        lifecycleScope.launch {
            val result = gigRepo.createGig(gig)
            result.fold(
                onSuccess = { gigId ->
                    Toast.makeText(
                        this@PostGigActivity,
                        "🎉 Gig published! Buyers can now find your service.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@PostGigActivity,
                        "Failed to publish: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.btnPublish.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.tvDraftStatus.text = "Draft"
                }
            )
        }
    }
}
