package com.example.myweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.myweather.network.WeatherApiService
import com.example.myweather.network.WeatherProperty
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Retrofit

var currentLocation: WeatherRawData = WeatherRawData(GpsRawData(0.0,0.0,0.0))

class SplashScreen : AppCompatActivity()
{
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }

    private val locationCallback = object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult)
        {

        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    fun getLastLocation()
    {
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
                    }
                } //END OF addOnSuccessListener
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        if(requestCode == 1010)
        {
            if(grantResults.isNotEmpty() &&
                grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}