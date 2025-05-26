package com.example.qweather.view_models.city_details_weather_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.city_by_location.CityWeatherModel
import com.example.qweather.repository.CityWeatherRepository
import kotlinx.coroutines.launch

class CityWeatherViewModel(private val cityRepository: CityWeatherRepository): ViewModel() {
    var _cityWeather = MutableLiveData<CityWeatherModel>()
    val cityWeather: MutableLiveData<CityWeatherModel> = _cityWeather

    suspend fun getCityWeather(lat: Double, long: Double) {



    }






}