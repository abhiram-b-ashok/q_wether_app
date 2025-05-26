package com.example.qweather.repository

import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class CitiesRepository(private val okHttpClient: OkHttpClient) {

    val isPostRequest = true

    suspend fun getCities(): NetworkResult<CitiesResponse> = withContext(Dispatchers.IO) {
        try {
            val url = buildUrl("cities").build()

            //  Step 1: Build proper request based on API requirements
            val request = if (isPostRequest) {
                // For POST requests with JSON body
                val requestBody = """
                {
                    "key": "value"  // Add required parameters
                }
            """.trimIndent().toRequestBody("application/json".toMediaType())

                Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build()
            } else {
                // For GET requests with query params
                Request.Builder()
                    .url(url)
                    .get()
                    .build()
            }

            //  Step 2: Add auth header if required
            request.newBuilder()
                .addHeader("Authorization", "Bearer YOUR_TOKEN") // If needed
                .build()

            //  Step 3: Execute request
            okHttpClient.newCall(request).execute().use { response ->
                when {
                    !response.isSuccessful -> {
                        NetworkResult.Error("API failed (${response.code}): ${response.message}")
                    }
                    response.body == null -> {
                        NetworkResult.Error("Empty response body")
                    }
                    else -> {
                        // Parse response
                        val moshi = Moshi.Builder()
                            .add(KotlinJsonAdapterFactory())
                            .build()

                        val adapter = moshi.adapter(CitiesResponse::class.java)
                        val responseData = adapter.fromJson(response.body!!.source())

                        if (responseData?.response?.status == true) {
                            NetworkResult.Success(responseData)
                        } else {
                            NetworkResult.Error("API returned false status")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }}

