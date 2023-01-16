package com.example.myweather

import android.util.Log
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.databinding.ActivityWeatherBinding
import com.example.myweather.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class WeatherActivity : AppCompatActivity()
{
    private lateinit var viewModel: WeatherViewModel
    private lateinit var rGeoviewModel: RevGeoViewModel
    private lateinit var databinding: ActivityWeatherBinding

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val jsonResponse = intent.getSerializableExtra("WeatherResponse") as WeatherProperty
        val RGeoRes = intent.getSerializableExtra("RevGeoResponse") as ReverseGeoProperty

        Log.v("SIZE", " Weather ${weatherList.size}")
        databinding = ActivityWeatherBinding.inflate(LayoutInflater.from(baseContext))

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        databinding.weatherLayout = jsonResponse
        databinding.weatherViewModel = viewModel
        databinding.revGeoLayout = RGeoRes
        databinding.lifecycleOwner = this
        setContentView(databinding.root)
    }
}