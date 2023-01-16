package com.example.myweather.network

import com.google.gson.annotations.SerializedName

data class CurrentWeatherData(
    @SerializedName("temperature")
    val temperature: Float,
    @SerializedName("weathercode")
    val weatherCode: Float,
    @SerializedName("windspeed")
    val windspeed: Float,
    @SerializedName("winddirection")
    val winddirection: Int,
    @SerializedName("time")
    val time: String,
) :java.io.Serializable
