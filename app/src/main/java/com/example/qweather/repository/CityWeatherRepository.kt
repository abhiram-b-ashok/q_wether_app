package com.example.qweather.repository

import android.util.Log
import com.example.qweather.data.models.cities_weather.WeatherResponseWrapper
import com.example.qweather.data.models.cities_weather.WeatherResult
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

class WeatherRepository {

    fun fetchWeatherData(
        lat: Double,
        lon: Double,
        isQatar: Boolean,
        callback: (NetworkResult<WeatherResult>) -> Unit
    ) {
        val requestBody = FormBody.Builder()
            .add("lat", lat.toString())
            .add("long", lon.toString())
            .add("is_qatar", if (isQatar) "1" else "0")
            .build()

        val url = buildUrl("location/intnl_city").build()

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
                        Log.d("WeatherRepo", "Raw JSON: $json")

                        try {
                            val gson = Gson()
                            val wrapper = gson.fromJson(json, WeatherResponseWrapper::class.java)
                            val result = wrapper.response.result
                            callback(NetworkResult.Success(result))
                        } catch (e: Exception) {
                            callback(NetworkResult.Error("Parsing error: ${e.message}"))
                        }
                    } ?: callback(NetworkResult.Error("Empty response body"))
                } else {
                    callback(NetworkResult.Error("HTTP error: ${response.code}"))
                }
            }
        })
    }

    object WeatherRepositoryProvider {
        val repository by lazy { WeatherRepository() }
    }

}
