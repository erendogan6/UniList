package com.erendogan6.unilist.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.unilist.adaptor.ProvinceAdapter
import com.erendogan6.unilist.databinding.ActivityMainBinding
import com.erendogan6.unilist.viewmodel.UniversityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UniversityListViewModel by viewModels()
    private val adapter = ProvinceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        collectProvinces()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun collectProvinces() {
        lifecycleScope.launch {
            viewModel.provinces.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}