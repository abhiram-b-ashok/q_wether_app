package com.example.qweather.repository

import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ContactUsRepository {
    fun sendContactMessage(
        name: String,
        email: String,
        subject: String,
        message: String
    ): NetworkResult<String> {
        val url = buildUrl("user/savecontactmessage").build()

        val jsonBody = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("subject", subject)
            put("message", message)
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        return try {
            val response = NetworkHandler.okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                NetworkResult.Success("Message sent successfully")
            } else {
                NetworkResult.Error("Error: ${response.code}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Exception: ${e.localizedMessage}")
        }
    }




}