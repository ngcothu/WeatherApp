package com.example.myweather.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService
{
    @GET("v1/forecast")
    fun getProperties(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: List<String>,
        @Query("daily") daily: List<String>,
        @Query("current_weather") current_weather: String,
        @Query("timezone") timezone: String
    ): Call<WeatherProperty>
}
object MarsApi {

}