package com.example.myweather

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecyclerAdapter(private val weatherList:java.util.ArrayList<WeatherData>)
    : RecyclerView.Adapter<RecyclerAdapter.WeatherViewHolder>()
{

    var onItemClick: ((WeatherData) -> Unit)? = null
    class WeatherViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var textViewCity : TextView = itemView.findViewById(R.id.twCity)
        var textViewWeather : TextView = itemView.findViewById(R.id.twWeather)
        var textViewTemp : TextView = itemView.findViewById(R.id.twTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int)
    {
        val weather = weatherList[position]
        holder.textViewCity.text = weather.zWeatherData.city
        holder.textViewTemp.text = weather.zWeatherData.temp.toString()
        holder.textViewWeather.text = weather.zWeatherData.weather

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(weather)
        }
    }

    override fun getItemCount(): Int
    {
        return weatherList.size
    }
}