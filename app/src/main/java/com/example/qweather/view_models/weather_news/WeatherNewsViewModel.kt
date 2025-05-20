package com.example.qweather.view_models.weather_news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.weather_news.WeatherNewsModel
import com.example.qweather.repository.WeatherNewsRepository

class WeatherNewsViewModel(weatherNewsRepository: WeatherNewsRepository) : ViewModel() {
    private val _weatherNewsList = MutableLiveData<List<WeatherNewsModel>>()
    val weatherNewsList: LiveData<List<WeatherNewsModel>> = _weatherNewsList

    fun getWeatherNews() {

    }



}