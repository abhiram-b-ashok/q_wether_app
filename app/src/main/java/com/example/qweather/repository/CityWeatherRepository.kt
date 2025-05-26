package com.example.qweather.repository

import com.example.qweather.data.network.ApiResponse
import com.example.qweather.data.network.api_call.getCityDetailsApi

class CityWeatherRepository {
    suspend fun getCityWeather(lat: Double, long: Double): ApiResponse {

        return TODO("Provide the return value")
    }
}