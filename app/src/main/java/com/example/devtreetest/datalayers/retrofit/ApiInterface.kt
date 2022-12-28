package com.example.devtreetest.datalayers.retrofit


import com.example.devtreetest.core.Constant
import com.example.devtreetest.mapUtil.model.Direction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(Constant.DIRECTION_API_URL)
    suspend fun getDirection(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("waypoints") waypoints: String?,
        @Query("mode") transportMode: String?,
        @Query("key") apiKey: String?
    ): Response<Direction?>?

    @GET(Constant.DIRECTION_API_URL)
    suspend fun getDirection(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("mode") transportMode: String?,
        @Query("key") apiKey: String?
    ): Response<Direction?>?
}


