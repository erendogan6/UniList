package com.erendogan6.unilist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erendogan6.unilist.model.University

@Dao interface UniversityDao {
    @Query("SELECT * FROM favorite_universities") suspend fun getAllFavorites(): List<University>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertFavorite(university: University)
    @Delete suspend fun deleteFavorite(university: University)
}