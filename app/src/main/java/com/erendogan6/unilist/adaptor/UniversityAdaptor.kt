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

            binding.universityPhone.setOnClickListener {
                val phoneNumber = university.university.phone
                if (phoneNumber != "-") {
                    startDialer(phoneNumber)
                }
            }

            val favoriteIconResId = if (university.university.isFavorite) R.drawable.favorite_icon else R.drawable.unfavorite_icon
            binding.favoriteIcon.setImageResource(favoriteIconResId)

            binding.favoriteIcon.setOnClickListener {
                val newFavoriteStatus = !university.university.isFavorite
                onFavoriteClicked(university.university)
                university.university.isFavorite = newFavoriteStatus
                binding.favoriteIcon.setImageResource(if (newFavoriteStatus) R.drawable.favorite_icon else R.drawable.unfavorite_icon)
            }

            binding.universityWebsite.setOnClickListener {
                val uni = university.university
                val url = uni.website
                val name = uni.name
                if (url != "-") {
                    val action = ListFragmentDirections.actionListFragmentToWebViewFragment(url, name)
                    itemView.findNavController().navigate(action)
                }
            }

            val detailsAreEmpty = with(university.university) {
                phone == "-" && fax == "-" && website == "-" && adress == "-" && rector == "-"
            }

            binding.expandIcon.visibility = if (detailsAreEmpty) View.INVISIBLE else View.VISIBLE

            binding.universityName.text = university.university.name
            binding.universityPhone.text = "Telefon: ${university.university.phone}"
            binding.universityFax.text = "Fax: ${university.university.fax}"
            binding.universityWebsite.text = "Website: ${university.university.website}"
            binding.universityAdress.text = "Adres: ${university.university.adress}"
            binding.universityRector.text = "Rektör: ${university.university.rector}"
            updateExpandIcon(university.isExpanded)
            binding.detailsLayout.visibility = if (university.isExpanded) View.VISIBLE else View.GONE

            binding.universityAdress.setOnClickListener {
                val address = university.university.adress
                val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(itemView.context.packageManager) != null) {
                    itemView.context.startActivity(mapIntent)
                } else {
                    Toast.makeText(itemView.context, "Google Maps uygulaması yüklü değil", Toast.LENGTH_SHORT).show()
                }
            }

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