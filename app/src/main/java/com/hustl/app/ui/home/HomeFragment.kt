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
import com.google.android.material.chip.Chip
import com.hustl.app.R
import com.hustl.app.adapters.GigAdapter
import com.hustl.app.adapters.SellerAdapter
import com.hustl.app.data.model.Gig
import com.hustl.app.data.repository.GigRepository
import com.hustl.app.databinding.FragmentHomeBinding
import com.hustl.app.ui.gigs.GigDetailActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val gigRepo = GigRepository()
    private var allGigs = listOf<Gig>()
    private lateinit var gridAdapter: GigAdapter
    private lateinit var featuredAdapter: GigAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupSearch()
        setupCategories()
        loadGigs()
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

        val sellerAdapter = SellerAdapter(gigRepo.getSampleGigs().take(5).map {
            Triple(it.sellerName, it.category, it.rating)
        })
        binding.rvSellers.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sellerAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterGigs(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCategories() {
        val categories = listOf("All", "Design", "Development", "Writing", "Marketing", "Video", "Music")
        categories.forEach { cat ->
            val chip = Chip(requireContext()).apply {
                text = cat
                isCheckable = true
                isChecked = cat == "All"
                setChipBackgroundColorResource(R.color.chip_selector)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
                setOnClickListener { filterByCategory(cat) }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun loadGigs() {
        lifecycleScope.launch {
            allGigs = gigRepo.getSampleGigs()
            featuredAdapter.submitList(allGigs.take(5))
            gridAdapter.submitList(allGigs)
            binding.tvGigCount.text = "${allGigs.size} gigs"
        }
    }

    private fun filterByCategory(category: String) {
        val filtered = if (category == "All") allGigs
        else allGigs.filter { it.category == category }
        gridAdapter.submitList(filtered)
        binding.tvGigCount.text = "${filtered.size} gigs"
        binding.tvGridTitle.text = if (category == "All") "All Gigs" else category
    }

    private fun filterGigs(query: String) {
        val filtered = if (query.isEmpty()) allGigs
        else allGigs.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.sellerName.contains(query, ignoreCase = true)
        }
        gridAdapter.submitList(filtered)
        binding.tvGigCount.text = "${filtered.size} gigs"
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
