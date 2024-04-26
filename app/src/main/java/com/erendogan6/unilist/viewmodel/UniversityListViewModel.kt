package com.erendogan6.unilist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel class UniversityListViewModel @Inject constructor(private val repository: UniversityRepository) : ViewModel() {
    private val _provincesFlow = MutableStateFlow<PagingData<ProvinceWithExpansion>?>(null)
    val provincesFlow: MutableStateFlow<PagingData<ProvinceWithExpansion>?> = _provincesFlow

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            repository.getProvincesStream().cachedIn(viewModelScope).collectLatest { pagingData ->
                val favoriteNames = repository.getAllFavorites().map { it.university.name }.toSet()
                val updatedData = pagingData.map { provinceWithExpansion ->
                    val updatedUniversities = provinceWithExpansion.universities.map { universityWithExpansion ->
                        universityWithExpansion.copy(university = universityWithExpansion.university.copy(isFavorite = universityWithExpansion.university.name in favoriteNames))
                    }
                    provinceWithExpansion.copy(universities = updatedUniversities)
                }
                _provincesFlow.value = updatedData
            }
        }
    }
}