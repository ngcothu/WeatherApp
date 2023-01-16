package com.example.myweather.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GeocodingProperty(
    @SerializedName("results")
    val results: List<GeocodingResult>,
): Serializable