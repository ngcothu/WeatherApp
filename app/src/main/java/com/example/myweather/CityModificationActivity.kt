package com.example.myweather

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.network.GeocodingApiService
import com.example.myweather.network.GeocodingProperty
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList


class CityModificationActivity : AppCompatActivity()
{
    var weatherListModify = weatherList.toMutableList()
    private lateinit var recyclerViewMod: RecyclerView
    private lateinit var modListRAdapter: ModListRAdapter


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_modification)

        recyclerViewMod = findViewById(R.id.rvMod)
        recyclerViewMod.setHasFixedSize(true)
        recyclerViewMod.layoutManager = LinearLayoutManager(this)


        val editText = findViewById<EditText>(R.id.etSC)
        val searchButtion = findViewById<Button>(R.id.btSearch)

        modListRAdapter = ModListRAdapter(weatherList)
        recyclerViewMod.adapter = modListRAdapter

        editText.setOnClickListener{
            editText.text.clear()
        }
        searchButtion.setOnClickListener{
            val cityName = editText.text.toString()
            if(cityName.isNotEmpty())
            {
                getLatLongOfCity(cityName, weatherList.size)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.place_menu, menu)
        return true
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
                        Log.v("EDIT", "Add $pos ${weatherList.size}")
                        weatherList.add(WeatherData(ZWeatherData(pos, city, "NEW_NULL", -300.0, Geocode.results[0].latitude, Geocode.results[0].longitude)))
                        Log.v("EDIT", "New $pos ${weatherList.size}")
                        weatherList[pos].zWeatherData.geocodingProperty = Geocode
                        callStatus = true

                        modListRAdapter.notifyDataSetChanged()
                        Log.v("EDIT", "Done $pos ${weatherList[pos].zWeatherData.geocodingProperty.results[0].latitude}")
                    }
                } else {
                    Log.e("EDIT", "ALLER_FORERES_NICHT_ERFORGT")
                }
            }

            override fun onFailure(call: Call<GeocodingProperty>, t: Throwable) {
                Log.e("EDIT", "ALLER_NICHT_ERFROGT_RESPONSE")
                t.message?.let { Log.d("MainActivity", it) }
            }
        })
        return callStatus
    }
}