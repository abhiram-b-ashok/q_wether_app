package com.example.qweather.repository

import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.data.models.weather_news.WeatherNewsModel
import com.example.qweather.data.network.ApiResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.getActivitiesApi
import com.example.qweather.data.network.api_call.getWeatherApi

class ActivityRepository {
    suspend fun getActivities(): NetworkResult<List<ActivityModel>> {
        return try {
            val apiResponse = getActivitiesApi()
            if (apiResponse.status) {
                val activities = apiResponse.result?.activities?.data ?: emptyList()
                NetworkResult.Success(activities)
            } else {
                NetworkResult.Error("API returned failure status")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}

