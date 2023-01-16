package com.example.myweather.network

import com.google.gson.annotations.SerializedName

//temperature_2m,relativehumidity_2m,dewpoint_2m,apparent_temperature,precipitation,weathercode,surface_pressure
data class HourlyData(
    @SerializedName("time")
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature: List<Float>,
    @SerializedName("relativehumidity_2m")
    val relativeHumidity_2m: List<Int>,
    @SerializedName("dewpoint_2m")
    val dewPoint_2m: List<Float>,
    @SerializedName("apparent_temperature")
    val apparent_temperature: List<Float>,
    @SerializedName("precipitation")
    val precipitation: List<Float>,
    @SerializedName("weathercode")
    val weathercode: List<Int>,
    @SerializedName("surface_pressure")
    val surface_pressure: List<Float>,
) : java.io.Serializable
