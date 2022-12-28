package com.example.devtreetest.datalayers.data.dao

import androidx.room.*
import com.example.devtreetest.datalayers.data.entity.Place

@Dao
interface PlaceDao {

    @Query("SELECT * FROM place")
    suspend fun getAll(): List<Place>

    @Insert
    suspend fun insert(place: Place)

    @Update
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)

}