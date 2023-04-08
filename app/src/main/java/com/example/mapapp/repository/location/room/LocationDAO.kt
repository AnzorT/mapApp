package com.example.mapapp.repository.location.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDAO {
    @Query("SELECT * FROM locations")
    fun getAll(): List<LastLocation>

    @Insert
    fun addLocation(location: LastLocation)

    @Query("DELETE FROM locations WHERE id = :itemId")
    fun deleteItemById(itemId: String)
}