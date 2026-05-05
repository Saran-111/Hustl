package com.hustl.app.ui.orders

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
import com.hustl.app.data.repository.OrderRepository
import com.hustl.app.databinding.FragmentOrdersBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderRepo: OrderRepository
    private lateinit var authRepo: AuthRepository
    private lateinit var orderAdapter: OrderAdapter
    private var allOrders: List<Order> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderRepo = OrderRepository(requireContext())
        authRepo = AuthRepository(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter(emptyList())
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        observeOrders()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                applyFilter()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeOrders() {
        val currentUser = authRepo.currentUser
        lifecycleScope.launch {
            orderRepo.getMyOrders(currentUser?.userId ?: "user1").collectLatest { orders ->
                allOrders = orders
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val status = when (binding.tabLayout.selectedTabPosition) {
            0 -> "active"
            1 -> "completed"
            2 -> "pending"
            else -> "active"
        }
        val filtered = allOrders.filter { it.status == status }
        orderAdapter.updateList(filtered)
        binding.tvEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        binding.rvOrders.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
