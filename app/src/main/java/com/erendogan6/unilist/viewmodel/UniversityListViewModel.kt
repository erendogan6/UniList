package com.erendogan6.unilist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.repository.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel class UniversityListViewModel @Inject constructor(private val repository: UniversityRepository) : ViewModel() {
    val provinces: Flow<PagingData<ProvinceWithExpansion>> = repository.getProvincesStream().cachedIn(viewModelScope)
}