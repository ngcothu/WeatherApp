package com.example.myweather

import android.os.Parcel
import android.os.Parcelable
import com.example.myweather.network.GeocodingProperty
import com.example.myweather.network.ReverseGeoProperty
import com.example.myweather.network.WeatherProperty

data class ZWeatherData(val _id: Int, var city: String?, var weather: String?, var temp: Double, var lat: Float, var long: Float) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readFloat(),
    )
    {
    }

    lateinit var weatherResponse : WeatherProperty
    lateinit var reverseGeoProperty: ReverseGeoProperty
    lateinit var geocodingProperty: GeocodingProperty

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeInt(_id)
        parcel.writeString(city)
        parcel.writeString(weather)
        parcel.writeDouble(temp)
        parcel.writeFloat(lat)
        parcel.writeFloat(long)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ZWeatherData>
    {
        override fun createFromParcel(parcel: Parcel): ZWeatherData
        {
            return ZWeatherData(parcel)
        }

        override fun newArray(size: Int): Array<ZWeatherData?>
        {
            return arrayOfNulls(size)
        }
    }

}

class WeatherData (val zWeatherData: ZWeatherData)


