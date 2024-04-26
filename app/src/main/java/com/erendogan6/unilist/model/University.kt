package com.erendogan6.unilist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_universities") data class University(
    @PrimaryKey val name: String, val phone: String, val fax: String, val website: String, val email: String, val adress: String, val rector: String, var isFavorite: Boolean = false)