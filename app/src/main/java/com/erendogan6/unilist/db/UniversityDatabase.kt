package com.erendogan6.unilist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erendogan6.unilist.model.University

@Database(entities = [University::class], version = 1, exportSchema = false) abstract class UniversityDatabase : RoomDatabase() {
    abstract fun getDao(): UniversityDao
}
