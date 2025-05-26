package com.example.qweather.data.network.api_call

import android.util.Log
import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkHandler.okHttpClient
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType

import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

suspend fun getCities(): NetworkResult<CitiesResponse> = withContext(Dispatchers.IO) {
    try {
        val url = buildUrl("cities").build()

        // Create JSON request body (if required)
        val requestBody = JSONObject()
            .put("key", "value") // Add required parameters
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody) // Use POST instead of GET
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer YOUR_TOKEN") // If needed
            .build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return@withContext NetworkResult.Error("API failed: ${response.code}")
        }

        val responseBody = response.body?.string()
        if (responseBody.isNullOrEmpty()) {
            return@withContext NetworkResult.Error("Empty response")
        }

        // Parse with Moshi
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(CitiesResponse::class.java)
        val citiesResponse = adapter.fromJson(responseBody)

        if (citiesResponse?.response?.status == true) {
            NetworkResult.Success(citiesResponse)
        } else {
            NetworkResult.Error("API returned false status")
        }
    } catch (e: Exception) {
        NetworkResult.Error("Network error: ${e.message}")
    }
}