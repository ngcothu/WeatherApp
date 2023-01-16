package com.example.myweather

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.network.WeatherProperty
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherViewModel : ViewModel(), Observable
{
    var isDataEmpty = MutableLiveData<Boolean>()
    @Bindable
    var weatherLayout = MutableLiveData<WeatherProperty>()


    init
    {
        isDataEmpty.value = false
    }
    fun windspeed (windspeed : Float) : String
    {
        return "$windspeed km/h"
    }
    fun winddirection (winddirection : Int) : String
    {
        if (winddirection in 335 .. 359 || winddirection in 0 .. 25)
        {
            return "$winddirection N"
        }
        else if (winddirection in 25..65)
        {
            return "$winddirection NE"
        }
        else if (winddirection in 65..115)
        {
            return "$winddirection E"
        }
        else if (winddirection in 115..155)
        {
            return "$winddirection SE"
        }
        else if (winddirection in 155..205)
        {
            return "$winddirection S"
        }
        else if (winddirection in 205..245)
        {
            return "$winddirection SW"
        }
        else if (winddirection in 245..295)
        {
            return "$winddirection W"
        }
        else if (winddirection in 295..335)
        {
            return "$winddirection NW"
        }
        else
            return "No Wind"
    }
    fun time(dateformat: String): String
    {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.GERMANY)
        val time = format.parse(dateformat)
        val timef = time.hours.toString() +":"+time.minutes.toString()
        return timef
    }
    fun temperature (temp: Float) : String
    {
        val tempString : String = temp.roundToInt().toString() + "°C"
        return tempString
    }

    fun WeatherCode (weather: Int) : String
    {
        val weatherCond : String = when(weather)
        {
            0 -> "Clear Sky"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"
            45 -> "Fog"
            48 -> "Rime fog"
            51 -> "Drizzle"
            53 -> "Moderate drizzle"
            55 -> "Dense drizzle"
            56 -> "Freezing drizzle"
            57 -> "Dense freezing drizzle"
            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"
            66 -> "Light freezing rain"
            67 -> "Heavy freezing rain"
            71 -> "Slight snow"
            73 -> "Moderate snow"
            75 -> "Heavy snow"
            77 -> "Snow grains"
            80 -> "Slight shower"
            81 -> "Moderate shower"
            82 -> "Violent shower"
            85 -> "Snow showers"
            86 -> "Heavy snow showers"
            95 -> "Thunderstorm"
            96 -> "Thunderstorm with hail"
            99 -> "Thunderstorm with heavy hail"
            else ->
            {
                "null"
            }
        }
        return weatherCond
    }

    fun feellike (temp: Float) : String
    {
        return "Feels like\n${temp.toInt()} °C"
    }
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?)
    {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?)
    {
    }
}