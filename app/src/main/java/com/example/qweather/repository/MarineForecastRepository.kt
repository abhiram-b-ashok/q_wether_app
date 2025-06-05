package com.example.qweather.repository

import com.example.qweather.data.models.marine_forecast.MarineForecast
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.fetchMarineForecast

class MarineForecastRepository {
    suspend fun getMarineForecast(): NetworkResult<List<MarineForecast>> {
        return try {
            val apiResponse = fetchMarineForecast()
            if (apiResponse.status) {
                val marineForecastList = apiResponse.result?.marineForecast?.data ?: emptyList()
                NetworkResult.Success(marineForecastList)
            } else {
                NetworkResult.Error("API returned failure status")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}