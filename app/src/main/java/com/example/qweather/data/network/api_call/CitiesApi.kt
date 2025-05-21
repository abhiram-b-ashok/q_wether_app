package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.cities.CitiesApiResponse
import com.example.qweather.data.models.cities.CitiesByType
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import okhttp3.Request

suspend fun fetchCities(): NetworkResult<CitiesApiResponse> {
    val url = buildUrl("cities").build().toString()
    val request = Request.Builder().url(url).build()

    return try {
        val response = NetworkHandler.okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            val jsonString = response.body?.string()
            if (jsonString != null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val adapter = moshi.adapter(CitiesApiResponse::class.java)

                val citiesApiResponse = adapter.fromJson(jsonString)

                if (citiesApiResponse != null && citiesApiResponse.response.status) {
                    NetworkResult.Success(citiesApiResponse)
                } else {
                    NetworkResult.Error("API status false or parsing failed")
                }
            } else {
                NetworkResult.Error("Empty response body")
            }
        } else {
            NetworkResult.Error("HTTP error code: ${response.code}")
        }
    } catch (e: Exception) {
        NetworkResult.Error("Exception: ${e.message}")
    }
}



