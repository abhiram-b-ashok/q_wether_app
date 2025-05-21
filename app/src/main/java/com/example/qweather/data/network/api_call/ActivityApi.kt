package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.activities.ActivitiesApiResponse
import com.example.qweather.data.models.activities.toActivitiesApiResponse
import com.example.qweather.data.models.weather_news.ResponseData
import com.example.qweather.data.models.weather_news.toResponseData
import com.example.qweather.data.network.ApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.text.ifEmpty

suspend fun getActivitiesApi(httpClient: OkHttpClient = NetworkHandler.okHttpClient): ActivitiesApiResponse =
    withContext(Dispatchers.IO) {
        try {
            val url = buildUrl("activities")
                .addQueryParameter("type", "outdoor")
                .addQueryParameter("page", "1")
                .build()

            val request = Request.Builder()
                .url(url)
                .post(RequestBody.create(null, ByteArray(0)))
                .build()

            val response = httpClient.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && body != null) {
                val jsonObject = JSONObject(body)
                return@withContext jsonObject.toActivitiesApiResponse()
            } else {
                throw Exception("API call failed: ${response.code}")
            }
        } catch (e: Exception) {
            throw e
        }
    }



