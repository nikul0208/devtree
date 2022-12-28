package com.example.devtreetest.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devtreetest.datalayers.data.AppDatabase
import com.example.devtreetest.ui.addLocation.viewmodel.AddLocationModel
import com.example.devtreetest.ui.map.viewmodel.MapViewModel
import com.example.devtreetest.ui.viewLocation.viewmodel.ViewLocationViewModel

class ViewModelFactory(private val appDatabase: AppDatabase) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddLocationModel::class.java)) {
            return AddLocationModel(appDatabase) as T
        }

        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(appDatabase) as T
        }

        if (modelClass.isAssignableFrom(ViewLocationViewModel::class.java)) {
            return ViewLocationViewModel(appDatabase) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}