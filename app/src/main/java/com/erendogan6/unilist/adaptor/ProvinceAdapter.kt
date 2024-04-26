package com.erendogan6.unilist.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.unilist.R
import com.erendogan6.unilist.databinding.ItemProvinceBinding
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.model.University

class ProvinceAdapter(var onFavoriteClicked: (University) -> Unit) : PagingDataAdapter<ProvinceWithExpansion, ProvinceAdapter.ProvinceViewHolder>(ProvinceComparator) {

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

    fun collapseAllItems() {
        val currentList = snapshot()
        currentList.forEach { item ->
            if (item != null) {
                if (item.isExpanded) {
                    item.isExpanded = false
                    notifyItemChanged(currentList.indexOf(item))
                }
                item.universities.forEach {
                    if (it.isExpanded) {
                        it.isExpanded = false
                        notifyItemChanged(currentList.indexOf(item))
                    }
                }
            }
        }
    }

    inner class ProvinceViewHolder(private val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root) {
        private var universityAdapter: UniversityAdapter? = null

        init {
            universityAdapter = UniversityAdapter(onFavoriteClicked)
            binding.universityList.layoutManager = LinearLayoutManager(binding.root.context)
            binding.universityList.adapter = universityAdapter

            binding.expandIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentProvince = getItem(position)
                    currentProvince?.let {
                        it.isExpanded = !it.isExpanded
                        updateExpandIcon(it.isExpanded)
                        notifyItemChanged(position)

                        if (it.isExpanded) {
                            universityAdapter?.submitList(it.universities)
                            binding.universityList.visibility = View.VISIBLE
                        } else {
                            binding.universityList.visibility = View.GONE
                        }
                    }
                }
            }
        }


        fun bind(province: ProvinceWithExpansion) {
            binding.provinceName.text = province.province.province
            updateExpandIcon(province.isExpanded)
            binding.expandIcon.visibility = if (province.universities.isEmpty()) View.GONE else View.VISIBLE

            if (province.isExpanded) {
                universityAdapter?.submitList(province.universities)
                binding.universityList.visibility = View.VISIBLE
            } else {
                binding.universityList.visibility = View.GONE
            }
        }

        private fun updateExpandIcon(isExpanded: Boolean) {
            binding.expandIcon.setImageResource(if (isExpanded) R.drawable.collapse_icon else R.drawable.expand_icon)
        }
    }


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

