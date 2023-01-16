package com.example.myweather.network

import com.google.gson.annotations.SerializedName

data class GeocodingResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Float,
    @SerializedName("longitude")
    val longitude: Float,
    @SerializedName("country")
    val country: String,
    @SerializedName("timezone")
    val timezone: String,
) : java.io.Serializable
