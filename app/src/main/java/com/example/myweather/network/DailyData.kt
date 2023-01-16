package com.example.myweather.network

import com.google.gson.annotations.SerializedName

//temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_sum,windspeed_10m_max,winddirection_10m_dominant,shortwave_radiation_sum
data class DailyData(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("temperature_2m_max")
    val temperature_2m_max: List<Float>,
    @SerializedName("temperature_2m_min")
    val temperature_2m_min: List<Float>,
    @SerializedName("sunrise")
    val sunrise: List<String>,
    @SerializedName("sunset")
    val sunset: List<String>,
    @SerializedName("precipitation_hours")
    val precipitation_hours: List<Int>,
    @SerializedName("windspeed_10m_max")
    val windspeed_10m_max: List<Float>,
    @SerializedName("winddirection_10m_dominant")
    val winddirection_10m_dominant: List<Int>,
    @SerializedName("shortwave_radiation_sum")
    val shortwave_radiation_sum: List<Float>,

) : java.io.Serializable
{
}