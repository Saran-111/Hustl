package com.hustl.app.ui.gigs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.hustl.app.data.model.Gig
import com.hustl.app.data.model.GigPackage
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.databinding.ActivityCreateGigBinding
import kotlinx.coroutines.launch

class CreateGigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGigBinding
    private val gigRepo = GigRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnPublish.setOnClickListener { publishGig() }
    }

    private fun publishGig() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()
        val basicPrice = binding.etBasicPrice.text.toString().trim().toIntOrNull() ?: 0
        val tags = binding.etTags.text.toString().split(",").map { it.trim() }.filter { it.isNotEmpty() }

        if (title.isEmpty()) { binding.etTitle.error = "Title is required"; return }
        if (description.isEmpty()) { binding.etDescription.error = "Description is required"; return }
        if (basicPrice == 0) { binding.etBasicPrice.error = "Set a price"; return }

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "user1"

        val gig = Gig(
            sellerId = uid,
            sellerName = "You",
            title = title,
            description = description,
            category = category,
            tags = tags,
            packages = listOf(
                GigPackage("Basic", basicPrice, "Standard delivery", 3, 2, listOf("As described")),
                GigPackage("Standard", basicPrice * 2, "Extended delivery", 2, 5, listOf("As described", "Extra revisions")),
                GigPackage("Premium", basicPrice * 3, "Priority delivery", 1, -1, listOf("As described", "Unlimited revisions", "Priority support"))
            )
        )

        binding.btnPublish.isEnabled = false
        lifecycleScope.launch {
            val result = gigRepo.createGig(gig)
            result.fold(
                onSuccess = {
                    Toast.makeText(this@CreateGigActivity, "Gig published successfully! 🎉", Toast.LENGTH_LONG).show()
                    finish()
                },
                onFailure = {
                    Toast.makeText(this@CreateGigActivity, "Published locally! Connect Firebase to save.", Toast.LENGTH_LONG).show()
                    finish()
                }
            )
        }
    }
}
