package com.erendogan6.unilist.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.unilist.databinding.FragmentSplashBinding
import com.erendogan6.unilist.model.ProvinceWithExpansion

class SplashAdapter : PagingDataAdapter<ProvinceWithExpansion, SplashAdapter.SplashViewHolder>(ProvinceComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplashViewHolder {
        val binding = FragmentSplashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SplashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SplashViewHolder, position: Int) {}

    inner class SplashViewHolder(binding: FragmentSplashBinding) : RecyclerView.ViewHolder(binding.root)
    companion object {
        val ProvinceComparator = object : DiffUtil.ItemCallback<ProvinceWithExpansion>() {
            override fun areItemsTheSame(oldItem: ProvinceWithExpansion, newItem: ProvinceWithExpansion): Boolean {
                return oldItem.province.id == newItem.province.id
            }

            override fun areContentsTheSame(oldItem: ProvinceWithExpansion, newItem: ProvinceWithExpansion): Boolean {
                return oldItem == newItem
            }
        }
    }
}