package com.erendogan6.unilist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erendogan6.unilist.db.UniversityDao
import com.erendogan6.unilist.model.ProvinceWithExpansion
import com.erendogan6.unilist.model.UniversityWithExpansion
import com.erendogan6.unilist.network.ApiService
import javax.inject.Inject

class ProvincePagingSource @Inject constructor(private val apiService: ApiService, private val universityDao: UniversityDao) : PagingSource<Int, ProvinceWithExpansion>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProvinceWithExpansion> {
        val pageNumber = params.key ?: 1
        return try {
            val response = apiService.getUniversities(pageNumber)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val favoriteNames = universityDao.getAllFavorites().map { it.name }.toSet()
                val expansion = data.data.map { province ->
                    ProvinceWithExpansion(province = province, isExpanded = false, universities = province.universities.map { university ->
                        UniversityWithExpansion(university.copy(isFavorite = university.name in favoriteNames), isExpanded = false)
                    })
                }
                LoadResult.Page(data = expansion, prevKey = if (pageNumber == 1) null else pageNumber - 1, nextKey = if (pageNumber < data.totalPage) pageNumber + 1 else null)
            } else {
                LoadResult.Error(Exception("API call was unsuccessful"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProvinceWithExpansion>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        page.prevKey?.let { return it + 1 }
        page.nextKey?.let { return it - 1 }
        return null
    }
}