package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.weather_news.ResponseData
import com.example.qweather.data.models.weather_news.toAPIResponse
import com.example.qweather.data.network.ApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

suspend fun getWeatherApi(httpClient: OkHttpClient = NetworkHandler.okHttpClient): ApiResponse = withContext(IO) {
    var code = 0
    var exception: Throwable? = null
    var message: String? = null
    var data: JSONObject? = null

    try {
        val url = buildUrl("news").build()

        // Optional: if API expects empty JSON body or specific params
        val requestBody = JSONObject().apply {
            // put("key", "value") if needed
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody) // POST instead of GET
            .addHeader("Content-Type", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        code = response.code
        message = response.message.ifEmpty { "Oops! Something went wrong" }

        if (code == 200) {
            data = response.body?.string()?.let { JSONObject(it) }
        } else {
            exception = Exception(message)
        }

    } catch (e: Exception) {
        exception = e
    }

    return@withContext ApiResponse(
        code = code,
        exception = exception,
        message = message,
        response = data?.toAPIResponse()?.response ?: ResponseData(false, null)
    )
}
