package com.example.qweather.data.network.api_call

import com.example.qweather.data.models.city_search.CitySearchApiResponse
import com.example.qweather.data.models.city_search.toCitySearchApiResponse
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

//suspend fun getSearchCities(input: String, httpClient: OkHttpClient = NetworkHandler.okHttpClient): CitySearchApiResponse =
//    withContext(Dispatchers.IO){
//        try {
//            val url = buildUrl("city/search")
//                .addQueryParameter(input, "qatar")
//                .build()
//
//
//            val request = Request.Builder()
//                .url(url)
//                .post(RequestBody.create(null, ByteArray(0)))
//                .build()
//
//            val response = httpClient.newCall(request).execute()
//            val body = response.body?.string()
//            if (response.isSuccessful && body != null) {
//                val jsonObject = JSONObject(body)
//                return@withContext jsonObject.toCitySearchApiResponse()
//            } else {
//                throw Exception("API call failed: ${response.code}")
//            }
//        }
//        catch (e: Exception) {
//            throw e
//        }
//
//}

suspend fun getSearchCities(
    input: String,
    httpClient: OkHttpClient = NetworkHandler.okHttpClient
): CitySearchApiResponse = withContext(Dispatchers.IO) {
    try {
        // ✅ Correct URL with query parameter
        val url = buildUrl("cities/search")
            .addQueryParameter("q", input)
            .build()

        // ✅ Optional: You can send an empty body if the API requires POST
        val requestBody = RequestBody.create(null, ByteArray(0))

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && responseBody != null) {
            val jsonObject = JSONObject(responseBody)
            return@withContext jsonObject.toCitySearchApiResponse()
        } else {
            throw Exception("API call failed: ${response.code} ${response.message}")
        }
    } catch (e: Exception) {
        throw e
    }
}
