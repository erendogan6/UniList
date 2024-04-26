package com.erendogan6.unilist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.unilist.adaptor.UniversityAdapter
import com.erendogan6.unilist.databinding.FragmentFavoritesBinding
import com.erendogan6.unilist.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: UniversityAdapter
    

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        favoritesViewModel.getFavorites()
        setupBackButton()
        setupRecyclerView()
        collectUniversities()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val expandedStates = adapter.getExpandedStates()
        outState.putBooleanArray("expanded_states", expandedStates)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getBooleanArray("expanded_states")?.let {
            adapter.restoreExpandedStates(it)
        }
    }

    private fun setupBackButton() {
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UniversityAdapter { university ->
            favoritesViewModel.toggleFavorite(university)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun collectUniversities() {
        lifecycleScope.launch {
            favoritesViewModel.favoritesList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.warningText.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.warningText.visibility = View.VISIBLE
                }
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}