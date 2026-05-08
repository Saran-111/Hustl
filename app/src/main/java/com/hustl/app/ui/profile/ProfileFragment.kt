package com.hustl.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.FragmentProfileBinding
import com.hustl.app.ui.profile.HustlrProfileActivity
import com.hustl.app.ui.auth.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var authRepo: AuthRepository
    private lateinit var orderRepo: OrderRepository
    private lateinit var gigRepo: GigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepo = AuthRepository(requireContext())
        orderRepo = OrderRepository(requireContext())
        gigRepo = GigRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()

        binding.menuOrders.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                com.hustl.app.R.id.bottom_nav
            ).selectedItemId = com.hustl.app.R.id.nav_orders
        }

        binding.menuViewProfile.setOnClickListener {
            val currentUser = authRepo.currentUser ?: return@setOnClickListener
            val intent = Intent(requireContext(), HustlrProfileActivity::class.java)
            intent.putExtra("hustlr_id", currentUser.userId)
            startActivity(intent)
        }

        binding.menuSaved.setOnClickListener {
            Toast.makeText(requireContext(), "Saved gigs coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuLogout.setOnClickListener {
            authRepo.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
    }

    private fun loadUserData() {
        val currentUser = authRepo.currentUser ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            val user = authRepo.getUserProfile(currentUser.userId)

            binding.tvName.text = user.name.ifEmpty { "User" }

            // Show role badge with hustlr terminology
            val roleBadge = if (user.role == "hustlr") "⚡ Hustlr" else "🛒 Buyer"
            binding.tvRole.text = "$roleBadge · Member since 2024"
            binding.tvLocation.text = user.location.ifEmpty { "Location not set" }
            binding.tvRating.text = String.format("%.1f", user.rating)

            if (user.role == "hustlr") {
                binding.menuViewProfile.visibility = View.VISIBLE
            }

            orderRepo.getMyOrders(user.userId).collectLatest { orders ->
                binding.tvTotalOrders.text = orders.size.toString()
                
                // Get actual review count from repository
                lifecycleScope.launch {
                    gigRepo.getReviewsForUser(user.userId).collectLatest { reviews ->
                        binding.tvReviews.text = reviews.size.toString()
                    }
                }

                if (user.role == "hustlr") {
                    binding.tvBalanceLabel.text = "Total Earnings"
                    val totalEarnings = orders.filter { it.status == "completed" && it.sellerId == user.userId }.sumOf { it.price }
                    binding.tvTotalSpent.text = "₹${"%,d".format(totalEarnings)}"
                    binding.tvBalanceFooter.text = "From ${orders.count { it.status == "completed" && it.sellerId == user.userId }} completed gigs"
                } else {
                    binding.tvBalanceLabel.text = "Total Spent"
                    val totalSpent = orders.filter { it.status == "completed" && it.buyerId == user.userId }.sumOf { it.price }
                    binding.tvTotalSpent.text = "₹${"%,d".format(totalSpent)}"
                    val activeCount = orders.count { (it.status == "active" || it.status == "pending") && it.buyerId == user.userId }
                    binding.tvBalanceFooter.text = "Across ${orders.count { it.buyerId == user.userId }} orders · $activeCount active"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
