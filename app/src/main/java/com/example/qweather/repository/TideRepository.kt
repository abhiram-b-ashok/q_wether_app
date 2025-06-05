package com.example.qweather.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object TideAPI {
    suspend fun getTidesData(): String? {
        try {
            val client = OkHttpClient()
            val baseUrl = "https://qweather.app/api/xml/tide_hourly"
            val request = Request.Builder().url(baseUrl).build()
            return  withContext(Dispatchers.IO){
                val response = client.newCall(request).execute()
                if(!response.isSuccessful) {
                    throw IOException("Current Weather Call : Unexpected Error")
                }
                response.body?.string().orEmpty()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

