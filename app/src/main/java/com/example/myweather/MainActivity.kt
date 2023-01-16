package com.example.myweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.network.*
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import kotlin.math.roundToInt

const val WEATHER_BASE_URL = "https://api.open-meteo.com/"
const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com"
const val REVGEO_BASE_URL = "https://api.bigdatacloud.net"

lateinit var weatherList: java.util.ArrayList<WeatherData>

class MainActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: RecyclerAdapter

    private lateinit var weatherResponse: WeatherProperty
    private lateinit var RGeoRes : ReverseGeoProperty
    private var mHandler: Handler? = null

    var getUpdated = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonClick = findViewById<Button>(R.id.bSW)

        val instanceWeatherList : List<WeatherData>

        weatherList = ArrayList()

        instanceWeatherList = getArrayList("com.example.myweather.preferenceWeather")
        if(instanceWeatherList.isEmpty())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            {
                getLastLocation()
            }
            weatherList.add(WeatherData(ZWeatherData(0,"Current City", "Cloudy", 0.0, currentLocation.rawData.lat.toFloat(), currentLocation.rawData.long.toFloat())));
        }
        else
        {
            weatherList = instanceWeatherList
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loop@ for ((count, items) in weatherList.withIndex())
        {
            var callst = false

            if(count == 0)
            {
                callst = getCityNameOfCurrent(currentLocation.rawData.lat.toFloat(), currentLocation.rawData.long.toFloat(), count)
                callst = getWeather(currentLocation.rawData.lat.toFloat(), currentLocation.rawData.long.toFloat(), count)
            }
            else if(count < weatherList.size)
            {
                callst = getCityNameOfCurrent(items.zWeatherData.lat, items.zWeatherData.long, count)
                callst = getWeather(items.zWeatherData.lat, items.zWeatherData.long, count)
            }
        }

        weatherAdapter = RecyclerAdapter(weatherList)
        recyclerView.adapter = weatherAdapter

        weatherAdapter.onItemClick = {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("weather", it.zWeatherData)
            intent.putExtra("WeatherResponse", it.zWeatherData.weatherResponse)
            intent.putExtra("RevGeoResponse", it.zWeatherData.reverseGeoProperty)
            startActivity(intent)
        }
        buttonClick.setOnClickListener {
            val intent = Intent(this, CityModificationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun WeatherCode (weather: Int) : String
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

    override fun onResume()
    {
        super.onResume()
        Log.e("HOLDER", "Back to main")
        var count = 0
        loop@ for (items in weatherList)
        {
            var callst = false

            if(items.zWeatherData.weather == "NEW_NULL")
            {
                items.zWeatherData.city?.let { getLatLongOfCity(it, count) }
            }
            count++
        }
        weatherAdapter.notifyDataSetChanged()
    }


    override fun onStop()
    {
        super.onStop()
        saveArrayList(weatherList,"com.example.myweather.preferenceWeather")
    }
    //"com.example.myweather.preferenceWeather"
    fun saveArrayList(list: java.util.ArrayList<WeatherData>, key: String?) {
        val prefs: SharedPreferences = getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun getArrayList(key: String?): java.util.ArrayList<WeatherData> {
        val prefs: SharedPreferences = getSharedPreferences(key, Context.MODE_PRIVATE)
        if(prefs.contains(key))
        {
            val gson = Gson()
            val json: String? = prefs.getString(key, null)
            val type: Type = object : TypeToken<java.util.ArrayList<WeatherData?>?>() {}.type
            return gson.fromJson(json, type)
        }
        else
        {
            val weatherl = java.util.ArrayList<WeatherData>()
            return weatherl
        }

    }
    private fun getLatLongOfCity(city: String, pos: Int) : Boolean
    {
        var callStatus = false
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GEOCODING_BASE_URL)
            .build()

        val retrofitService = retrofit.create(GeocodingApiService::class.java)

        val call: Call<GeocodingProperty> =
            retrofitService.getProperties(city)

        call.enqueue(object : Callback<GeocodingProperty>
        {
            override fun onResponse(call: Call<GeocodingProperty>, response: Response<GeocodingProperty>) {
                if (response.isSuccessful) {
                    val Geocode = response.body()
                    if (Geocode != null)
                    {
                        weatherList[pos].zWeatherData.geocodingProperty = Geocode
                        callStatus = true
                        getUpdated = true
                        val items = weatherList[pos]
                        weatherList[pos].zWeatherData.city = Geocode.results[0].name
                        weatherList[pos].zWeatherData.lat = Geocode.results[0].latitude
                        weatherList[pos].zWeatherData.long = Geocode.results[0].longitude

                        //attach another call of retrofit after Geocoding response
                        attachExecute(1, Geocode.results[0].latitude, Geocode.results[0].longitude, weatherList[pos], pos)

                        weatherAdapter.notifyDataSetChanged()
                        Log.v("HOLDER", "Done Latlong $pos ${weatherList[pos].zWeatherData.geocodingProperty.results[0].latitude}")
                    }
                } else {
                    getUpdated = false
                    Log.e("HOLDER", "ALLER_FORERES_NICHT_ERFORGT")
                }
            }

            override fun onFailure(call: Call<GeocodingProperty>, t: Throwable) {
                getUpdated = false
                Log.e("HOLDER", "ALLER_NICHT_ERFROGT_RESPONSE")
                t.message?.let { Log.d("MainActivity", it) }
            }
        })
        return callStatus
    }

    private fun getCityNameOfCurrent(lat: Float, long: Float, pos: Int = 0) : Boolean
    {
        var callStatus = false
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(REVGEO_BASE_URL)
            .build()

        val retrofitService = retrofit.create(RevGeoApiService::class.java)

        val call: Call<ReverseGeoProperty> =
            retrofitService.getProperties(lat, long)

        call.enqueue(object : Callback<ReverseGeoProperty>
        {
            override fun onResponse(call: Call<ReverseGeoProperty>, response: Response<ReverseGeoProperty>) {
                if (response.isSuccessful) {
                    val RevgeoRes = response.body()
                    if (RevgeoRes != null)
                    {
                        weatherList[pos].zWeatherData.reverseGeoProperty = RevgeoRes
                        RGeoRes = RevgeoRes
                        callStatus = true
                        if(pos == 0)
                        {
                            weatherList[pos].zWeatherData.city = "Current City: ${RevgeoRes.city}"
                            Log.v("HOLDER", "Done City Re $pos ${RevgeoRes.city}")
                            weatherAdapter.notifyItemChanged(pos)
                        }
                        else
                        {
                            weatherList[pos].zWeatherData.city = RevgeoRes.city
                            weatherAdapter.notifyItemChanged(pos)
                        }
                        Log.v("HOLDER", "Done City $pos ${weatherList[pos].zWeatherData._id}")
                    }
                } else {
                    getUpdated = false
                    Log.e("HOLDER", "ALLER_FORERES_NICHT_ERFORGT")
                }
            }

            override fun onFailure(call: Call<ReverseGeoProperty>, t: Throwable) {
                getUpdated = false
                Log.e("HOLDER", "ALLER_NICHT_ERFROGT_RESPONSE")
                t.message?.let { Log.d("MainActivity", it) }
            }
        })
        return callStatus
    }

    private fun getWeather(lat: Float, long: Float, pos: Int = 0) : Boolean
    {
        var callStatus = false
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WEATHER_BASE_URL)
            .build()

        val retrofitService = retrofit.create(WeatherApiService::class.java)
        val call: Call<WeatherProperty>

        call = retrofitService.getProperties(lat.toDouble(),
            long.toDouble(),
            listOf("temperature_2m","relativehumidity_2m","dewpoint_2m","apparent_temperature","precipitation","weathercode","surface_pressure"),
            listOf("weathercode","temperature_2m_max","temperature_2m_min","apparent_temperature_max","sunrise","sunset","precipitation_sum", "windspeed_10m_max", "winddirection_10m_dominant", "shortwave_radiation_sum"),
            "true",
            "Europe/Berlin")

        call.enqueue(object : Callback<WeatherProperty>
        {
            override fun onResponse(call: Call<WeatherProperty>, response: Response<WeatherProperty>) {
                if (response.isSuccessful) {
                    val forecastResponse = response.body()
                    if (forecastResponse != null)
                    {
                        weatherList[pos].zWeatherData.weatherResponse = forecastResponse
                        weatherResponse = forecastResponse
                        callStatus = true

                        weatherList[pos].zWeatherData.temp = weatherList[pos].zWeatherData.weatherResponse.hourlyWeather.temperature[0].roundToInt().toDouble()
                        weatherList[pos].zWeatherData.weather = WeatherCode(weatherList[pos].zWeatherData.weatherResponse.hourlyWeather.weathercode[0])
                        weatherAdapter.notifyItemChanged(pos)

                        Log.v("HOLDER", "Done Weather $pos ${weatherList[pos].zWeatherData._id}")
                    }
                    // Do something with the forecastResponse object
                    Log.e("HOLDER", "ALLEROK")
                } else {
                    Log.e("HOLDER", "ALLER_FORERES_NICHT_ERFORGT")
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<WeatherProperty>, t: Throwable) {
                // Handle failure
                Log.e("HOLDER", "ALLER_NICHT_ERFROGT_RESPONSE")
                t.message?.let { Log.d("MainActivity", it) };
            }
        })
        return callStatus
    }
    private fun attachExecute(idp: Int, lat: Float, long: Float, items: WeatherData, pos: Int)
    {
        if(idp == 0)
        {
            //do nothing
        }
        else if (idp == 1)
        {
            getCityNameOfCurrent(items.zWeatherData.lat, items.zWeatherData.long, pos)
            getWeather(items.zWeatherData.lat, items.zWeatherData.long, pos)
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun getLastLocation()
    {
        var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION), 1010)

        }
        Log.v("PROC", "Get here")
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isLocationEnabled)
        {
            Toast.makeText(this,"Please turn on your GPS", Toast.LENGTH_LONG).show()
            return
        }
        else
        {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if(location == null)
                    {
                        var locationRequest =
                            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000)
                                .setWaitForAccurateLocation(false)
                                .setMinUpdateIntervalMillis(500)
                                .setMaxUpdateDelayMillis(1000)
                                .build();
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    }
                    else
                    {
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent = Intent(this, MainActivity::class.java)
                            currentLocation.rawData.alt = location.altitude
                            currentLocation.rawData.lat = location.latitude
                            currentLocation.rawData.long = location.longitude
                            startActivity(intent)
                            finish()
                        }, 2000)
                        currentLocation.rawData.alt = location.altitude
                        currentLocation.rawData.lat = location.latitude
                        currentLocation.rawData.long = location.longitude
                    }
                } //END OF addOnSuccessListener
        }
    }
    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult)
        {

        }
    }
}