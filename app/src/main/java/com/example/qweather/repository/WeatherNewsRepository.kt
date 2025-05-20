package com.example.qweather.repository

import com.example.qweather.data.network.api_call.getWeatherApi


class WeatherNewsRepository {
    suspend fun getWeatherNews()
    {
        val response = getWeatherApi()


    }

}