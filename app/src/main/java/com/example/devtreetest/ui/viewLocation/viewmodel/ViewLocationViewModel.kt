package com.example.devtreetest.ui.viewLocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devtreetest.datalayers.data.AppDatabase
import com.example.devtreetest.datalayers.data.entity.Place
import com.example.devtreetest.utils.DistanceCalculationUtil
import kotlinx.coroutines.launch

class ViewLocationViewModel(private val appDatabase: AppDatabase): ViewModel() {

    private val liveDataPlace = MutableLiveData<List<Place>>()

    init {
        fetchLocations()
    }
    fun getLocations(): LiveData<List<Place>> {
        return liveDataPlace
    }

    fun fetchLocations() {
        viewModelScope.launch {
            liveDataPlace.postValue(appDatabase.placeDao().getAll())
        }
    }

    fun sortBy(sortBy: Int) {

        viewModelScope.launch {
            val items = appDatabase.placeDao().getAll()
            when(sortBy) {
                1 -> {
                    liveDataPlace.postValue(items.sortedBy { DistanceCalculationUtil.distance(items[0].latitude!!, items[0].longitude!!,it.latitude!!,it.longitude!!) })
                }
                2 -> {
                    liveDataPlace.postValue(items.sortedByDescending { DistanceCalculationUtil.distance(items[0].latitude!!, items[0].longitude!!,it.latitude!!,it.longitude!!) })
                }
            }
        }


    }

    fun deleteLocation(location: Place) {
        viewModelScope.launch {
            appDatabase.placeDao().delete(location)
            fetchLocations()
        }
    }
}