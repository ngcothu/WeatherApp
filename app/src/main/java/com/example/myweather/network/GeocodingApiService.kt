package com.example.myweather.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService
{
    @GET("v1/search")
    fun getProperties(
        @Query("name") city: String,
    ): Call<GeocodingProperty>
}