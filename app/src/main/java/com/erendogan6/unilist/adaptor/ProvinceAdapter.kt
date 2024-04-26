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
        currentList.forEachIndexed { index, item ->
            var notify = false
            if (item?.isExpanded == true) {
                item.isExpanded = false
                notify = true
            }
            item?.universities?.forEach {
                if (it.isExpanded) {
                    it.isExpanded = false
                    notify = true
                }
            }
            if (notify) notifyItemChanged(index)
        }
    }

    inner class ProvinceViewHolder(private val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root) {

        private val universityAdapter: UniversityAdapter by lazy {
            UniversityAdapter(onFavoriteClicked).apply {
                binding.universityList.layoutManager = LinearLayoutManager(binding.root.context)
                binding.universityList.adapter = this
            }
        }

        init {
            binding.expandIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentProvince = getItem(position)
                    currentProvince?.let {
                        it.isExpanded = !it.isExpanded
                        updateExpandIcon(it.isExpanded)
                        notifyItemChanged(position)

                        handleUniversityListVisibility(it)
                    }
                }
            }
        }

        private fun handleUniversityListVisibility(province: ProvinceWithExpansion) {
            val isVisible = province.isExpanded
            universityAdapter.submitList(if (isVisible) province.universities else emptyList())
            binding.universityList.visibility = if (isVisible) View.VISIBLE else View.GONE
        }


        fun bind(province: ProvinceWithExpansion) {
            binding.provinceName.text = province.province.province
            updateExpandIcon(province.isExpanded)
            binding.expandIcon.visibility = if (province.universities.isEmpty()) View.GONE else View.VISIBLE

            handleUniversityListVisibility(province)
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

