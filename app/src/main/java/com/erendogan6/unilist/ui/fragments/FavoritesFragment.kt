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
import com.erendogan6.unilist.model.UniversityWithExpansion
import com.erendogan6.unilist.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: UniversityAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            initializeUI()
        }
        favoritesViewModel.getFavorites()
        observeFavorites()
        return binding.root
    }

    private fun FragmentFavoritesBinding.initializeUI() {
        setupBackButton()
        setupRecyclerView()
    }

    private fun FragmentFavoritesBinding.setupBackButton() {
        backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun FragmentFavoritesBinding.setupRecyclerView() {
        adapter = UniversityAdapter { university ->
            favoritesViewModel.toggleFavorite(university)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun observeFavorites() {
        lifecycleScope.launch {
            favoritesViewModel.favoritesList.observe(viewLifecycleOwner) { universities ->
                updateUIBasedOnFavorites(universities)
            }
        }
    }

    private fun updateUIBasedOnFavorites(universities: List<UniversityWithExpansion>) {
        val hasFavorites = universities.isNotEmpty()
        binding.recyclerView.visibility = if (hasFavorites) View.VISIBLE else View.GONE
        binding.warningText.visibility = if (hasFavorites) View.GONE else View.VISIBLE
        adapter.submitList(universities)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}