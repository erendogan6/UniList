package com.erendogan6.unilist.model

data class Province(
    val id: Int,
    val province: String,
    val universities: List<University>
)