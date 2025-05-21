package com.example.qweather.data.network.api_call


import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.buildUrl
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationApiResponse
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.toNotificationApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

suspend fun getNotificationsApi(
    page: Int,
    userId: String,
    lang: String,
    httpClient: OkHttpClient = NetworkHandler.okHttpClient
): NotificationApiResponse = withContext(Dispatchers.IO) {
    try {
        val url = buildUrl("user")
            .addPathSegment("getnotifications")
            .addQueryParameter("id", userId)
            .addQueryParameter("page", page.toString())
            .addQueryParameter("lang", lang)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(null, ByteArray(0)))
            .build()

        val response = httpClient.newCall(request).execute()
        val body = response.body?.string()

        if (response.isSuccessful && body != null) {
            val jsonObject = JSONObject(body)
            return@withContext jsonObject.toNotificationApiResponse()
        } else {
            throw Exception("API call failed: ${response.code}")
        }
    } catch (e: Exception) {
        throw e
    }
}
