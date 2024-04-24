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
import com.erendogan6.unilist.databinding.ItemUniversityBinding
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.model.UniversityWithExpansion

class ProvinceAdapter : PagingDataAdapter<ProvinceWithExpansion, ProvinceAdapter.ProvinceViewHolder>(ProvinceComparator) {

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

    inner class ProvinceViewHolder(private val binding: ItemProvinceBinding) : RecyclerView.ViewHolder(binding.root) {
        private var universityAdapter: UniversityAdapter? = null

        init {
            universityAdapter = UniversityAdapter()
            binding.universityList.layoutManager = LinearLayoutManager(binding.root.context)
            binding.universityList.adapter = universityAdapter

            binding.expandIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val province = getItem(position)
                    province?.let {
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
            binding.expandIcon.visibility = if (province.universities.isNullOrEmpty()) View.GONE else View.VISIBLE

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

class UniversityAdapter : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    private var universities: List<UniversityWithExpansion> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding = ItemUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val university = universities[position]
        holder.bind(university)
    }

    override fun getItemCount(): Int = universities.size

    fun submitList(list: List<UniversityWithExpansion>) {
        universities = list
        notifyDataSetChanged()
    }

    inner class UniversityViewHolder(private val binding: ItemUniversityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(university: UniversityWithExpansion) {

            val detailsAreEmpty = with(university.university) {
                phone == "-" && fax == "-" && website == "-" && adress == "-" && rector == "-"
            }

            binding.expandIcon.visibility = if (detailsAreEmpty) View.GONE else View.VISIBLE

            binding.universityName.text = university.university.name
            binding.universityPhone.text = "Telefon: ${university.university.phone}"
            binding.universityFax.text = "Fax: ${university.university.fax}"
            binding.universityWebsite.text = "Website: ${university.university.website}"
            binding.universityAdress.text = "Adres: ${university.university.adress}"
            binding.universityRector.text = "Rektör: ${university.university.rector}"
            updateExpandIcon(university.isExpanded)
            binding.detailsLayout.visibility = if (university.isExpanded) View.VISIBLE else View.GONE

            binding.expandIcon.setOnClickListener {
                if (detailsAreEmpty) return@setOnClickListener
                university.isExpanded = !university.isExpanded
                updateExpandIcon(university.isExpanded)
                notifyItemChanged(bindingAdapterPosition)
            }
        }

        private fun updateExpandIcon(isExpanded: Boolean) {
            binding.expandIcon.setImageResource(if (isExpanded) R.drawable.collapse_icon else R.drawable.expand_icon)
        }
    }
}
