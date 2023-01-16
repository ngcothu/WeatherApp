package com.example.myweather.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RevGeoApiService
{
    @GET("data/reverse-geocode-client")
    fun getProperties(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float
    ): Call<ReverseGeoProperty>
}