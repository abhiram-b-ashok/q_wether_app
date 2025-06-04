package com.example.qweather.repository

//import android.util.Log
//import com.example.qweather.data.models.tide.TideModel
//import com.example.qweather.data.network.NetworkHandler
//import com.example.qweather.data.network.NetworkResult
//import com.example.qweather.data.network.buildUrl
//import com.example.qweather.ui.dashboard.inner_fragments.tides.TideXmlDocument
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.Request
//import okhttp3.Response
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.IOException
//import java.io.StringReader
//
//class TideRepository { // areaId can be passed directly to buildUrl or not used here
//    // You could pass areaId to the fetch function if it's dynamic
//    // fun fetchTidesApi(areaId: String, callback: (NetworkResult<TideModel>) -> Unit)
//
//    fun fetchTidesApi(callback: (NetworkResult<TideModel>) -> Unit) {
//        // Using your provided buildUrl function
//        val url = buildUrl("xml/tide_hourly").build()
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        callback(NetworkResult.Loading())
//
//        NetworkHandler.okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                callback(NetworkResult.Error("Request failed: ${e.message}"))
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    response.body?.let { body ->
//                        val fetchedXml = body.string()
//                        Log.d("TideRepository", "Fetched XML: $fetchedXml")
//                        try {
//                            val parser = XmlPullParserFactory.newInstance().newPullParser()
//                            parser.setInput(StringReader(fetchedXml))
//
//                            val tideModel = TideXmlDocument.readXml(parser)
//
//                            if (tideModel != null) {
//                                // Return the complete TideModel
//                                callback(NetworkResult.Success(tideModel))
//
//                            } else {
//                                callback(NetworkResult.Error("XML parsing resulted in null data."))
//                            }
//
//                        } catch (e: Exception) {
//                            callback(NetworkResult.Error("XML parsing failed: ${e.message}"))
//                        }
//                    } ?: callback(NetworkResult.Error("Empty response body"))
//                } else {
//                    callback(NetworkResult.Error("HTTP error: ${response.code}"))
//                }
//            }
//        })
//    }
//}



import android.util.Log

import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.buildUrl
import com.example.qweather.ui.dashboard.inner_fragments.tides.TideXmlDocument

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import kotlin.coroutines.resume


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

