package com.erendogan6.unilist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.unilist.R
import com.erendogan6.unilist.adaptor.ProvinceAdapter
import com.erendogan6.unilist.databinding.FragmentListBinding
import com.erendogan6.unilist.viewmodel.FavoritesViewModel
import com.erendogan6.unilist.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: UniversityListViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: ProvinceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        collectProvinces()
        setupLoadStateListener()
        setupCollapseIcon()
        setupFavoriteIcon()
        return binding.root
    }


    private fun setupCollapseIcon() {
        binding.collapseIcon.setOnClickListener {
            adapter.collapseAllItems()
        }
    }

    private fun setupFavoriteIcon() {
        binding.favoriteIcon.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_favoritesFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProvinceAdapter(onFavoriteClicked = { university ->
            favoritesViewModel.toggleFavorite(university)
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    private fun setupLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun collectProvinces() {
        lifecycleScope.launch {
            listViewModel.provincesFlow.collectLatest { pagingData ->
                if (pagingData != null) {
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
