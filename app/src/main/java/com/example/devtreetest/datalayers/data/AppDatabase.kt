package com.example.devtreetest.datalayers.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.devtreetest.datalayers.data.dao.PlaceDao
import com.example.devtreetest.datalayers.data.entity.Place

@Database(entities = [Place::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

}