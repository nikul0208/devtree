package com.example.devtreetest.ui.addLocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devtreetest.datalayers.data.AppDatabase
import com.example.devtreetest.utils.Resource
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch

class AddLocationModel(private val appDatabase: AppDatabase): ViewModel() {

    private val liveDataPlaceAdded = MutableLiveData<Resource<Boolean>>()

    fun isPlaceAdded(): LiveData<Resource<Boolean>> {
        return liveDataPlaceAdded
    }

    fun fetchPlaceRequest(locationId: Int, placeID: String, placesClient: PlacesClient) {
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        var request: FetchPlaceRequest? = null
        request = FetchPlaceRequest.builder(placeID, placeFields)
            .build()
        if (request != null) {
            placesClient.fetchPlace(request).addOnSuccessListener { task ->
                addPlace(locationId,task)
            }.addOnFailureListener { e ->
                e.printStackTrace()
            }
        }
    }

    private fun addPlace(locationId: Int, response: FetchPlaceResponse) {

        viewModelScope.launch {

            if(locationId == 0) {
                appDatabase.placeDao().insert(com.example.devtreetest.datalayers.data.entity.Place(name = response.place.name,
                    address = response.place.address,
                    latitude = response.place.latLng?.latitude,
                    longitude = response.place.latLng?.longitude
                ))
            } else {
                appDatabase.placeDao().update(com.example.devtreetest.datalayers.data.entity.Place(id = locationId, name = response.place.name,
                    address = response.place.address,
                    latitude = response.place.latLng?.latitude,
                    longitude = response.place.latLng?.longitude
                ))
            }
            liveDataPlaceAdded.postValue(Resource.success(true))
        }
    }


}