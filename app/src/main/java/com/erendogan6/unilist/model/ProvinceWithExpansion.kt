package com.erendogan6.unilist.model

data class ProvinceWithExpansion(val province: Province, var isExpanded: Boolean = false, val universities: List<UniversityWithExpansion>)