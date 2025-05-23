package com.example.qweather.repository

import com.example.qweather.data.models.cities.CitiesApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.fetchCitiesApi
import com.example.qweather.data.network.buildUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Request

class CitiesRepository {

    suspend fun getCities(): NetworkResult<CitiesApiResponse> {
        return fetchCitiesApi()  // Your existing suspend function
    }
}


