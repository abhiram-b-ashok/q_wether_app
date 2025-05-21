package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.weather_news.ResponseData
import com.example.qweather.data.models.weather_news.toAPIResponse
import com.example.qweather.data.models.weather_news.toResponseData
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
    var exception: Throwable? = null
    var message: String? = null
    var responseData: ResponseData = ResponseData(false, null)

    try {
        val url = buildUrl("news").build()

        val requestBody = JSONObject().toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        val response = httpClient.newCall(request).execute()
        val bodyString = response.body?.string()
        if (response.isSuccessful && bodyString != null) {
            val json = JSONObject(bodyString)
            if (json.has("Response")) {
                val responseJson = json.getJSONObject("Response")
                responseData = responseJson.toResponseData()
            } else {
                message = "Invalid response format"
            }
        } else {
            message = response.message.ifEmpty { "Something went wrong" }
            exception = Exception(message)
        }

    } catch (e: Exception) {
        exception = e
        message = e.message
    }

    return@withContext ApiResponse(
        code = null,
        exception = exception,
        message = message,
        response = responseData
    )
}
