package com.erendogan6.unilist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.network.ApiService
import com.erendogan6.unilist.paging.ProvincePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class UniversityRepository @Inject constructor(private val apiService: ApiService) {
    fun getProvincesStream(pagingConfig: PagingConfig = defaultPagingConfig()): Flow<PagingData<ProvinceWithExpansion>> {
        return Pager(config = pagingConfig, pagingSourceFactory = { ProvincePagingSource(apiService) }).flow
    }

    private fun defaultPagingConfig() = PagingConfig(pageSize = 30, enablePlaceholders = true, maxSize = 90, prefetchDistance = 5, initialLoadSize = 30)
}
