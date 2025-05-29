package com.example.qweather.repository

import com.example.qweather.data.models.forecast.ForecastResponseWrapper
import com.example.qweather.data.models.forecast.ForecastResult
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ForecastRepository {
    fun fetchForecastData(
        cityId: Int,
        callback: (NetworkResult<ForecastResult>) -> Unit
    ) {
        val requestBody = FormBody.Builder()
            .add("city_id", cityId.toString())
            .build()

        val url = buildUrl("current_weather")
            .addQueryParameter("city_id", cityId.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        callback(NetworkResult.Loading())

        NetworkHandler.okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResult.Error("Request failed: ${e.message}"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { body ->
                        val json = body.string()
                        try {
                            val gson = Gson()
                            val wrapper = gson.fromJson(json, ForecastResponseWrapper::class.java)
                            val result = wrapper.response.result
                            callback(NetworkResult.Success(result))
                        } catch (e: Exception) {
                            callback(NetworkResult.Error("Parsing error: ${e.message}"))
                        }
                    }?: callback(NetworkResult.Error("Empty response body"))
                } else {
                    callback(NetworkResult.Error("HTTP error: ${response.code}"))
                }
            }

        })

    }

}