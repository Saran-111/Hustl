package com.hustl.app.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hustl.app.adapters.OrderAdapter
import com.hustl.app.data.model.Order
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.data.repository.NotificationRepository
import com.hustl.app.databinding.FragmentOrdersBinding
import com.hustl.app.ui.gigs.PostGigActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderRepo: OrderRepository
    private lateinit var authRepo: AuthRepository
    private lateinit var gigRepo: GigRepository
    private lateinit var notifyRepo: NotificationRepository
    private lateinit var orderAdapter: OrderAdapter
    private var allOrders: List<Order> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderRepo = OrderRepository(requireContext())
        authRepo = AuthRepository(requireContext())
        gigRepo = GigRepository()
        notifyRepo = NotificationRepository()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = authRepo.currentUser
        orderAdapter = OrderAdapter(currentUser?.userId ?: "", emptyList()) { order, action ->
            viewLifecycleOwner.lifecycleScope.launch {
                when(action) {
                    "mark_review" -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            orderRepo.updateOrderStatus(order.orderId, "under_review")
                            notifyRepo.sendNotification(
                                order.buyerId,
                                "Order Ready for Review!",
                                "The Hustlr has submitted the work for '${order.gigTitle}'. Please verify to release funds.",
                                "order"
                            )
                        }
                    }
                    "verify" -> {
                        orderRepo.updateOrderStatus(order.orderId, "completed")
                        android.widget.Toast.makeText(requireContext(), "Order Verified & Funds Released!", android.widget.Toast.LENGTH_SHORT).show()
                    }
                    "revision" -> {
                        val input = android.widget.EditText(requireContext())
                        input.hint = "What needs to be changed?"
                        android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Request Revision")
                            .setView(input)
                            .setPositiveButton("Submit") { _, _ ->
                                val note = input.text.toString().trim()
                                if (note.isNotEmpty()) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        orderRepo.requestRevision(order.orderId, note)
                                        notifyRepo.sendNotification(
                                            order.sellerId,
                                            "Revision Requested",
                                            "The buyer requested changes for '${order.gigTitle}'. Note: $note",
                                            "order"
                                        )
                                    }
                                }
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                    "cancel" -> {
                        android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Cancel Order")
                            .setMessage("Are you sure you want to cancel this order?")
                            .setPositiveButton("Yes, Cancel") { _, _ ->
                                viewLifecycleOwner.lifecycleScope.launch {
                                    orderRepo.cancelOrder(order.orderId)
                                    android.widget.Toast.makeText(requireContext(), "Order cancelled.", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                            .setNegativeButton("No", null)
                            .show()
                    }
                }
            }
        }
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        setupHustlrDashboard()
        observeOrders()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) { applyFilter() }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupHustlrDashboard() {
        val user = authRepo.currentUser ?: return

        if (user.role == "hustlr") {
            binding.cardHustlrDashboard.visibility = View.VISIBLE

            // Wire up "Post Gig" button
            binding.btnPostGig.setOnClickListener {
                startActivity(Intent(requireContext(), PostGigActivity::class.java))
            }

            // Load hustlr stats (active gig count + earnings)
            viewLifecycleOwner.lifecycleScope.launch {
                // Count active gigs this hustlr has posted
                val allGigs = gigRepo.getAllGigs().first()
                val myActiveGigs = allGigs.count { it.sellerId == user.userId && it.isActive }
                binding.tvActiveGigs.text = myActiveGigs.toString()

                // Total earnings from completed orders as seller
                orderRepo.getMyOrders(user.userId).collect { orders ->
                    val earnings = orders
                        .filter { it.sellerId == user.userId && it.status == "completed" }
                        .sumOf { it.price }
                    binding.tvTotalEarnings.text = "₹${"%,d".format(earnings)}"
                }
            }
        } else {
            binding.cardHustlrDashboard.visibility = View.GONE
        }
    }

    private fun observeOrders() {
        val currentUser = authRepo.currentUser
        viewLifecycleOwner.lifecycleScope.launch {
            orderRepo.getMyOrders(currentUser?.userId ?: "user1").collectLatest { orders ->
                allOrders = orders
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val statuses = when (binding.tabLayout.selectedTabPosition) {
            0 -> listOf("active")
            1 -> listOf("completed", "cancelled")
            2 -> listOf("under_review")
            else -> listOf("active")
        }
        val filtered = allOrders.filter { it.status in statuses }
        orderAdapter.updateList(filtered)
        binding.tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.rvOrders.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
