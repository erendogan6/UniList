package com.erendogan6.unilist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erendogan6.unilist.model.University
import com.erendogan6.unilist.model.UniversityWithExpansion
import com.erendogan6.unilist.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class FavoritesViewModel @Inject constructor(private val repository: UniversityRepository) : ViewModel() {
    private val _favoritesList = MutableLiveData<List<UniversityWithExpansion>>()
    val favoritesList: LiveData<List<UniversityWithExpansion>> = _favoritesList

    fun getFavorites() {
        viewModelScope.launch {
            _favoritesList.value = repository.getAllFavorites()
        }
    }

    private fun insertFavorite(university: University) {
        viewModelScope.launch {
            repository.insertFavorite(university)
        }
    }

    private fun deleteFavorite(university: University) {
        viewModelScope.launch {
            repository.deleteFavorite(university)
        }
    }

    fun toggleFavorite(university: University) {
        viewModelScope.launch {
            if (university.isFavorite) {
                deleteFavorite(university)
            } else {
                university.isFavorite = true
                insertFavorite(university)
            }
            getFavorites()
        }
    }
}