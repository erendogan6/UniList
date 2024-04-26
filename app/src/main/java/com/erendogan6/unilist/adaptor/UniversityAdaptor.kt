package com.erendogan6.unilist.adaptor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.erendogan6.unilist.R
import com.erendogan6.unilist.databinding.ItemUniversityBinding
import com.erendogan6.unilist.model.University
import com.erendogan6.unilist.model.UniversityWithExpansion
import com.erendogan6.unilist.ui.fragments.ListFragmentDirections

class UniversityAdapter(private val onFavoriteClicked: (University) -> Unit) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {


    private var universities: List<UniversityWithExpansion> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding = ItemUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(binding)
    }

    fun getExpandedStates(): BooleanArray {
        return universities.map { it.isExpanded }.toBooleanArray()
    }

    fun restoreExpandedStates(expandedStates: BooleanArray) {
        universities.forEachIndexed { index, universityWithExpansion ->
            universityWithExpansion.isExpanded = expandedStates.getOrElse(index) { false }
        }
        notifyDataSetChanged()
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

        private fun startDialer(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            itemView.context.startActivity(intent)
        }

        fun bind(university: UniversityWithExpansion) {
            with(binding) {
                universityPhone.setOnClickListener { if (university.university.phone != "-") startDialer(university.university.phone) }
                updateFavoriteIcon(university.university.isFavorite)

                favoriteIcon.setOnClickListener {
                    toggleFavorite(university.university)
                    updateFavoriteIcon(university.university.isFavorite)
                }

                universityWebsite.setOnClickListener {
                    navigateToWebsite(university.university)
                }

                val detailsAreEmpty = checkDetailsEmpty(university.university)
                expandIcon.visibility = if (detailsAreEmpty) View.INVISIBLE else View.VISIBLE

                universityName.text = university.university.name
                universityPhone.text = "Telefon: ${university.university.phone}"
                universityFax.text = "Fax: ${university.university.fax}"
                universityWebsite.text = "Website: ${university.university.website}"
                universityAdress.text = "Adres: ${university.university.adress}"
                universityRector.text = "Rektör: ${university.university.rector}"

                updateExpandIcon(university.isExpanded)
                detailsLayout.visibility = if (university.isExpanded) View.VISIBLE else View.GONE
                universityAdress.setOnClickListener { openMap(university.university.adress) }
                expandIcon.setOnClickListener {
                    if (!detailsAreEmpty) {
                        toggleDetailsVisibility(university)
                        notifyItemChanged(bindingAdapterPosition)
                    }
                }
            }
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            binding.favoriteIcon.setImageResource(if (isFavorite) R.drawable.favorite_icon else R.drawable.unfavorite_icon)
        }

        private fun toggleFavorite(university: University) {
            val newFavoriteStatus = !university.isFavorite
            onFavoriteClicked(university)
            university.isFavorite = newFavoriteStatus
        }

        private fun navigateToWebsite(uni: University) {
            if (uni.website != "-") {
                val action = ListFragmentDirections.actionListFragmentToWebViewFragment(uni.website, uni.name)
                itemView.findNavController().navigate(action)
            }
        }

        private fun openMap(address: String) {
            if (address != "-") {
                val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                    setPackage("com.google.android.apps.maps")
                }

                if (mapIntent.resolveActivity(itemView.context.packageManager) != null) {
                    itemView.context.startActivity(mapIntent)
                } else {
                    Toast.makeText(itemView.context, "Google Maps uygulaması yüklü değil", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun toggleDetailsVisibility(university: UniversityWithExpansion) {
            university.isExpanded = !university.isExpanded
            updateExpandIcon(university.isExpanded)
        }

        private fun checkDetailsEmpty(uni: University): Boolean = listOf(uni.phone, uni.fax, uni.website, uni.adress, uni.rector).all { it == "-" }

        private fun updateExpandIcon(isExpanded: Boolean) {
            binding.expandIcon.setImageResource(if (isExpanded) R.drawable.collapse_icon else R.drawable.expand_icon)
        }
    }
}