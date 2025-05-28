package com.example.qweather.repository

import android.util.Log
import com.example.qweather.data.models.moon_phase.MoonPhaseModel
import com.example.qweather.data.models.moon_phase.MoonPhaseResponseWrapper
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class MoonPhaseRepository {

    fun fetchMoonPhaseData(
        cityId: Int,
        callback: (NetworkResult<MoonPhaseModel>) -> Unit
    ) {
        val url = buildUrl("cw_moon_phase/city")
            .addQueryParameter("city_id", cityId.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
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
                        Log.d("MoonPhaseRepo", "Raw JSON: $json")

                        try {
                            val gson = Gson()
                            val wrapper = gson.fromJson(json, MoonPhaseResponseWrapper::class.java)
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
}
