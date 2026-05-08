package com.hustl.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.hustl.app.R
import com.hustl.app.adapters.GigAdapter
import com.hustl.app.adapters.SellerAdapter
import com.hustl.app.data.model.Gig
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.databinding.FragmentHomeBinding
import com.hustl.app.ui.gigs.GigDetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var gigRepo: GigRepository
    private lateinit var authRepo: AuthRepository
    private var allGigs = listOf<Gig>()
    private lateinit var gridAdapter: GigAdapter
    private lateinit var featuredAdapter: GigAdapter
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gigRepo = GigRepository()
        authRepo = AuthRepository(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupSearch()
        setupCategories()
        observeGigs()
        setupClickListeners()
        updateUserProfile()

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        updateUserProfile()
        observeGigs() // Re-triggers the Flow collection
    }

    private fun updateUserProfile() {
        val user = authRepo.currentUser
        if (user != null) {
            val firstName = user.name.split(" ").firstOrNull() ?: "there"
            binding.tvGreeting.text = "Hello, $firstName! 👋"
        }
    }

    private fun setupClickListeners() {
        binding.btnProfile.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_profile
        }

        binding.btnNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationsActivity::class.java))
        }
        
        binding.btnExplore.setOnClickListener {
            // Smooth scroll to the gigs section
            binding.scrollView.smoothScrollTo(0, binding.tvGridTitle.top)
        }
        
        binding.tvSeeAll.setOnClickListener {
            binding.scrollView.smoothScrollTo(0, binding.tvGridTitle.top)
        }
    }

    private fun setupAdapters() {
        featuredAdapter = GigAdapter(isHorizontal = true) { gig -> openGig(gig) }
        binding.rvFeatured.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = featuredAdapter
        }

        gridAdapter = GigAdapter(isHorizontal = false) { gig -> openGig(gig) }
        binding.rvGigs.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gridAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observeGigs() {
        viewLifecycleOwner.lifecycleScope.launch {
            val currentUser = authRepo.currentUser
            gigRepo.getAllGigs().collectLatest { gigs ->
                // Filter out gigs created by the current user
                val displayGigs = gigs.filter { it.sellerId != currentUser?.userId }
                allGigs = displayGigs
                updateFeatured(displayGigs)
                applyFilters()
                
                val sellers = gigs.distinctBy { it.sellerId }.take(5).map {
                    Triple(it.sellerName, it.category, it.rating)
                }
                binding.rvSellers.apply {
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = SellerAdapter(sellers)
                }

                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun updateFeatured(gigs: List<Gig>) {
        featuredAdapter.submitList(gigs.filter { it.rating >= 4.8 }.sortedByDescending { it.createdAt }.take(5))
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCategories() {
        val categories = listOf("All", "Design", "Development", "Writing", "Marketing", "Video", "Music")
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { cat ->
            val chip = Chip(requireContext()).apply {
                text = cat
                isCheckable = true
                isChecked = cat == currentCategory
                setChipBackgroundColorResource(R.color.chip_selector)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
                setOnClickListener { 
                    currentCategory = cat
                    applyFilters()
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun applyFilters() {
        val query = binding.etSearch.text.toString()
        val filtered = allGigs.filter { gig ->
            (currentCategory == "All" || gig.category == currentCategory) &&
            (query.isEmpty() || gig.title.contains(query, ignoreCase = true) || gig.sellerName.contains(query, ignoreCase = true))
        }
        gridAdapter.submitList(filtered)
        binding.tvGigCount.text = "${filtered.size} gigs found"
        binding.tvGridTitle.text = if (currentCategory == "All") "All Gigs" else "$currentCategory Gigs"
    }

    private fun openGig(gig: Gig) {
        val intent = Intent(requireContext(), GigDetailActivity::class.java)
        intent.putExtra("gig_id", gig.gigId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
