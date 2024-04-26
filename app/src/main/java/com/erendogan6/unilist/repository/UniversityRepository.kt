package com.erendogan6.unilist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erendogan6.unilist.db.UniversityDao
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.model.University
import com.erendogan6.unilist.model.UniversityWithExpansion
import com.erendogan6.unilist.network.ApiService
import com.erendogan6.unilist.paging.ProvincePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class UniversityRepository @Inject constructor(private val apiService: ApiService, private val universityDao: UniversityDao) {
    fun getProvincesStream(pagingConfig: PagingConfig = provincesPagingConfig()): Flow<PagingData<ProvinceWithExpansion>> {
        return Pager(config = pagingConfig, pagingSourceFactory = { ProvincePagingSource(apiService) }).flow
    }

    private fun provincesPagingConfig() = PagingConfig(pageSize = 30, enablePlaceholders = true, maxSize = 90, prefetchDistance = 10, initialLoadSize = 30)

    suspend fun insertFavorite(university: University) {
        universityDao.insertFavorite(university)
    }

    suspend fun deleteFavorite(university: University) {
        universityDao.deleteFavorite(university)
    }

    suspend fun getAllFavorites(): List<UniversityWithExpansion> = withContext(Dispatchers.IO) {
        universityDao.getAllFavorites().map {
            UniversityWithExpansion(it, false)
        }
    }

    suspend fun isFavorite(universityName: String): Boolean {
        return universityDao.isFavorite(universityName)
    }
}
