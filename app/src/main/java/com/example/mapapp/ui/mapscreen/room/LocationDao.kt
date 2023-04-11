package com.example.mapapp.ui.mapscreen.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAll(): List<UserLocations>

    @Insert
    fun addLocation(location: UserLocations)

    @Query("DELETE FROM locations WHERE id = :itemId")
    fun deleteItemById(itemId: String)
}