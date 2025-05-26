package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.city_by_location.CityWeatherApiResponse
import com.example.qweather.data.models.city_by_location.toCityWeatherApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

suspend fun getCityDetailsApi(
    lat: Double,
    long:Double,
    httpClient: OkHttpClient = NetworkHandler.okHttpClient
): CityWeatherApiResponse = withContext(Dispatchers.IO) {
    try {
        val url = buildUrl("location")
            .addPathSegment("intnl_city")
            .addQueryParameter("lat", lat.toString())
            .addQueryParameter("long", long.toString())
            .addQueryParameter("is_qatar", "1")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(null, ByteArray(0)))
            .build()

        val response = httpClient.newCall(request).execute()
        val body = response.body?.string()

        if (response.isSuccessful && body != null) {
            val jsonObject = JSONObject(body)
            return@withContext jsonObject.toCityWeatherApiResponse()
        } else {
            throw Exception("API call failed: ${response.code}")
        }

    } catch (e: Exception) {
        throw e
    }
}