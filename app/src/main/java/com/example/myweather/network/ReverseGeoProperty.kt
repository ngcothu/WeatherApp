package com.example.myweather.network

import com.google.gson.annotations.SerializedName

data class ReverseGeoProperty(
    @SerializedName("latitude")
    var latitude: Float,
    @SerializedName("longitude")
    var longitude: Float,
    @SerializedName("city")
    var city: String,
    @SerializedName("countryName")
    var countryName: String,
) : java.io.Serializable
