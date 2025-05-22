package com.example.qweather.repository

import com.example.qweather.data.models.cities.CitiesApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Request

class CitiesRepository {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(CitiesApiResponse::class.java)

    fun getCities(): NetworkResult<CitiesApiResponse> {
        return try {
            val url = buildUrl("cities").build()
            val request = Request.Builder().url(url).build()
            val response = NetworkHandler.okHttpClient.newCall(request).execute()

            val body = response.body?.string()
            val result = adapter.fromJson(body ?: "")

            if (result != null && result.response.status) {
                NetworkResult.Success(result)
            } else {
                NetworkResult.Error("Invalid response from server")
            }

        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }
}

