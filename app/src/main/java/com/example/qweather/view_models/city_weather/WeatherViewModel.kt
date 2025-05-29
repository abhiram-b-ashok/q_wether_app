package com.example.qweather.view_models.city_weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.cities_weather.WeatherResult
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherResult = MutableLiveData<WeatherResult?>()
    val weatherResult: LiveData<WeatherResult?> get() = _weatherResult

    fun loadWeather(lat: Double, lon: Double, isQatar: Boolean) {
        repository.fetchWeatherData(lat, lon, isQatar) { result ->
            when (result) {
                is NetworkResult.Success -> _weatherResult.postValue(result.data)
                is NetworkResult.Error -> Log.e("WeatherViewModel", result.message ?: "Unknown error")
                else -> Unit
            }
        }
    }
}










