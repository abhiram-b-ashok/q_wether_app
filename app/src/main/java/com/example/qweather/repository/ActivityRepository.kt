package com.example.qweather.repository

import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.getActivitiesApi

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

