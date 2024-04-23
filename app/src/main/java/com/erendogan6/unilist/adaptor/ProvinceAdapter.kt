package com.erendogan6.unilist.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.unilist.databinding.ItemProvinceBinding
import com.erendogan6.unilist.model.Province

class ProvinceAdapter : PagingDataAdapter<Province, ProvinceAdapter.ProvinceViewHolder>(ProvinceComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHolder {
        val binding = ItemProvinceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProvinceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProvinceViewHolder, position: Int) {
        val province = getItem(position)
        province?.let {
            holder.bind(it)
        }
    }

    class ProvinceViewHolder(private val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(province: Province) {
            binding.provinceName.text = province.province
        }
    }

    
    companion object {
        val ProvinceComparator = object : DiffUtil.ItemCallback<Province>() {
            override fun areItemsTheSame(oldItem: Province, newItem: Province): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Province, newItem: Province): Boolean {
                return oldItem == newItem
            }
        }
    }
}