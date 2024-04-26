package com.erendogan6.unilist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.erendogan6.unilist.R
import com.erendogan6.unilist.adaptor.SplashAdapter
import com.erendogan6.unilist.databinding.FragmentSplashBinding
import com.erendogan6.unilist.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UniversityListViewModel by activityViewModels()
    private val adapter = SplashAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingState()
        setupLoadStateListener()
    }

    private fun setupLoadStateListener() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                navigateToListFragment()
            }
        }
    }

    private fun navigateToListFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_listFragment)
    }


    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.provincesFlow.collectLatest {
                delay(1000)
                if (it != null) {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}