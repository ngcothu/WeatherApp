package com.example.myweather.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService
{
    @GET("v1/search")
    fun getProperties(
        @Query("name") name: String,
    ): Call<GeocodingProperty>
}