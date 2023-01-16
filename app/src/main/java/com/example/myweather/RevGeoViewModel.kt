package com.example.myweather

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.network.ReverseGeoProperty


class RevGeoViewModel : ViewModel(), Observable
{
    var isDataEmpty = MutableLiveData<Boolean>()
    @Bindable
    var revGeoLayout = MutableLiveData<ReverseGeoProperty>()

    init
    {
        isDataEmpty.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?)
    {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?)
    {
    }

}