package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.marine_forecast.MarineForecastApiResponse
import com.example.qweather.data.models.marine_forecast.toMarineForecastApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

suspend fun fetchMarineForecast(httpClient: OkHttpClient = NetworkHandler.okHttpClient): MarineForecastApiResponse =
    withContext(Dispatchers.IO)
    {
        try {
            val url = buildUrl("marine/forecast/27")
                .build()
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = httpClient.newCall(request).execute()
            val body = response.body?.string()
            if (response.isSuccessful && body != null) {
                val jsonObject = JSONObject(body)
                return@withContext jsonObject.toMarineForecastApiResponse()
            }
            else {
                throw Exception("API call failed: ${response.code}")
            }
        }
        catch(e: Exception) {
            throw e
        }
    }
