package com.example.myweather

data class GpsRawData(var lat: Double, var alt: Double, var long: Double)
data class RawJData(var current_weather: Map<String, Any>,
                    var hourly: Map<String, Any>,
                    var daily: Map<String, Any>)

class WeatherRawData(var rawData: GpsRawData)
