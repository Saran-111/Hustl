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
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.FragmentProfileBinding
import com.hustl.app.ui.auth.LoginActivity
import com.hustl.app.ui.gigs.CreateGigActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val authRepo = AuthRepository()
    private val orderRepo = OrderRepository()

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

        binding.menuCreateGig.setOnClickListener {
            startActivity(Intent(requireContext(), CreateGigActivity::class.java))
        }

        binding.menuSaved.setOnClickListener {
            Toast.makeText(requireContext(), "Saved gigs coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuPayments.setOnClickListener {
            Toast.makeText(requireContext(), "Payment methods coming soon!", Toast.LENGTH_SHORT).show()
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
        
        lifecycleScope.launch {
            val user = authRepo.getUserProfile(currentUser.uid)
            val orders = orderRepo.getMyOrders(user.userId)
            
            binding.tvName.text = user.name.ifEmpty { "User" }
            binding.tvRole.text = "${user.role.replaceFirstChar { it.uppercase() }} · Member since 2024"
            binding.tvLocation.text = user.location.ifEmpty { "Location not set" }
            
            binding.tvTotalOrders.text = orders.size.toString()
            binding.tvRating.text = String.format("%.1f", user.rating)
            binding.tvReviews.text = (orders.size / 2).toString() // Simplified logic

            if (user.role == "seller") {
                binding.tvBalanceLabel.text = "Total Earnings"
                val totalEarnings = orders.filter { it.status == "completed" }.sumOf { it.price }
                binding.tvTotalSpent.text = "₹${"%,d".format(totalEarnings)}"
                binding.tvBalanceFooter.text = "From ${orders.count { it.status == "completed" }} completed orders"
                binding.menuCreateGig.visibility = View.VISIBLE
            } else {
                binding.tvBalanceLabel.text = "Total Spent"
                val totalSpent = orders.sumOf { it.price }
                binding.tvTotalSpent.text = "₹${"%,d".format(totalSpent)}"
                val activeCount = orders.count { it.status == "active" || it.status == "pending" }
                binding.tvBalanceFooter.text = "Across ${orders.size} orders · $activeCount active"
                binding.menuCreateGig.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
