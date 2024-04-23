package com.erendogan6.unilist.model

data class ProvinceData(
    val currentPage: Int,
    val totalPage: Int,
    val total: Int,
    val itemPerPage: Int,
    val pageSize: Int,
    val data: List<Province>
)
