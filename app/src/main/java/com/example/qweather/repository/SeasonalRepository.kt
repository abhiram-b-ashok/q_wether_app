package com.example.qweather.repository

import android.util.Log
import com.example.qweather.data.models.seasonal.SeasonalModel
import com.example.qweather.data.models.seasonal.SeasonalResponseWrapper
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class SeasonalRepository {
    fun fetchSeasonalData(callback: (NetworkResult<SeasonalModel>) -> Unit) {
        val url = buildUrl("seasonal_data")
            .build()
        val emptyBody = "".toRequestBody(null)
        val request = Request.Builder()
            .url(url)
            .post(emptyBody)
            .build()
        callback(NetworkResult.Loading())

        NetworkHandler.okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(NetworkResult.Error("Request failed: ${e.message}"))
                Log.e("SeasonalRepo", "OkHttp onFailure", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { body ->
                        val json = body.string()
                        Log.d("SeasonalRepo", "Raw JSON: $json")

                        try {
                            val gson = Gson()
                            val wrapper = gson.fromJson(json, SeasonalResponseWrapper::class.java)
                            val result = wrapper.response.result
                            callback(NetworkResult.Success(result))
                        } catch (e: Exception) {
                            callback(NetworkResult.Error("Parsing error: ${e.message}"))
                        }
                    }?: run {
                        Log.e("SeasonalRepo", "Successful response but body was null")
                        callback(NetworkResult.Error("Successful response but empty body"))
                    }

                } else {
                    val code = response.code
                    val message = response.message
                    var errorBodyString: String? = null
                    try {
                        errorBodyString = response.body?.string()
                    } catch (ex: Exception) {
                        Log.w("SeasonalRepo", "Could not read error body", ex)
                    }
                    Log.e("SeasonalRepo", "HTTP Unsuccessful. Code: $code, Message: $message, Error Body: $errorBodyString")
                    callback(NetworkResult.Error("HTTP error $code: $message"))
                }

                }

        })
    }
}