package com.example.qweather.data.network.api_call

import android.util.Log
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

suspend fun fetchCitiesApi(): NetworkResult<CitiesApiResponse> {
    val url = buildUrl("cities").build().toString()
    Log.d("CitiesRepository", "Fetching cities from URL: $url")

    val request = Request.Builder().url(url).build()

    return try {
        val response = NetworkHandler.okHttpClient.newCall(request).execute()
        Log.d("CitiesRepository", "HTTP Response code: ${response.code}")

        if (response.isSuccessful) {
            val jsonString = response.body?.string()
            Log.d("CitiesRepository", "Raw JSON response: $jsonString")

            if (jsonString != null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val adapter = moshi.adapter(CitiesApiResponse::class.java)

                val citiesApiResponse = adapter.fromJson(jsonString)

                if (citiesApiResponse != null && citiesApiResponse.response.status) {
                    Log.d("CitiesRepository", "Parsed response is successful")
                    NetworkResult.Success(citiesApiResponse)
                } else {
                    Log.e("CitiesRepository", "API status false or parsing failed")
                    NetworkResult.Error("API status false or parsing failed")
                }
            } else {
                Log.e("CitiesRepository", "Empty response body")
                NetworkResult.Error("Empty response body")
            }
        } else {
            Log.e("CitiesRepository", "HTTP error code: ${response.code}")
            NetworkResult.Error("HTTP error code: ${response.code}")
        }
    } catch (e: Exception) {
        Log.e("CitiesRepository", "Exception during API call", e)
        NetworkResult.Error("Exception: ${e.message}")
    }
}



