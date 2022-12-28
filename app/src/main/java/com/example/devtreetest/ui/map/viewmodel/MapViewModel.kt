package com.example.devtreetest.ui.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devtreetest.datalayers.data.AppDatabase
import com.example.devtreetest.datalayers.data.entity.Place
import com.example.devtreetest.datalayers.retrofit.ApiInterface
import com.example.devtreetest.datalayers.retrofit.RetrofitProvider
import com.example.devtreetest.mapUtil.model.Direction
import com.example.devtreetest.utils.DistanceCalculationUtil
import com.example.devtreetest.utils.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapViewModel(private val appDatabase: AppDatabase): ViewModel() {

    private val liveDataDirection = MutableLiveData<Resource<Direction>>()
    private val liveDataLocations = MutableLiveData<List<Place>>()

    fun getLocations(): LiveData<List<Place>> {
        return liveDataLocations
    }

    fun fetchLocations() {
        viewModelScope.launch {
            liveDataLocations.postValue(appDatabase.placeDao().getAll())
        }
    }
    fun getDirection(): LiveData<Resource<Direction>> {
        return liveDataDirection
    }
    fun requestDirection(serverKey: String) {

        liveDataDirection.postValue(Resource.loading())
        val apiService = RetrofitProvider.createService(ApiInterface::class.java)
        viewModelScope.launch {

            liveDataLocations.value?.let { locations ->
                val response = if(locations.size == 2) {
                    apiService.getDirection(locations[0].latitude.toString() + "," +locations[0].longitude.toString(),
                        locations[1].latitude.toString() + "," +locations[1].longitude.toString(),
                        "driving",
                        serverKey
                    )
                } else {

                    val wayPoints = mutableListOf<LatLng>()

                    for(item in locations) {
                        wayPoints.add(LatLng(item.latitude!!, item.longitude!!))
                    }

                    apiService.getDirection(locations.first().latitude.toString() + "," +locations.first().longitude,
                        locations.last().latitude.toString() + "," +locations.last().longitude,
                        waypointsToString(wayPoints),
                        "driving",
                        serverKey
                    )
                }

                response?.let {
                    if(it.isSuccessful) {
                        liveDataDirection.postValue(Resource.success(it.body()))
                    } else {
                        liveDataDirection.postValue(Resource.error(it.message()))
                    }
                }
            }

        }
    }

    private fun waypointsToString(waypoints: List<LatLng>?): String? {
        if (waypoints != null && !waypoints.isEmpty()) {
            val string = StringBuilder("optimize:true|")
            string.append(waypoints[0].latitude).append(",").append(waypoints[0].longitude)
            for (i in 1 until waypoints.size) {
                string.append("|").append(waypoints[i].latitude).append(",")
                    .append(waypoints[i].longitude)
            }
            return string.toString()
        }
        return null
    }

}