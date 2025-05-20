package com.example.qweather.data.network

import okhttp3.HttpUrl
import okhttp3.OkHttpClient


const val SCHEME = "https"
const val HOST = "qweather.app"
const val PATH = "api"

fun buildUrl(pathSegment: String):HttpUrl.Builder{
return HttpUrl
.Builder()
.scheme(SCHEME)
.host(HOST)
.addPathSegment(PATH)
.addPathSegment(pathSegment)

}

object NetworkHandler {
    lateinit var okHttpClient: OkHttpClient
    fun init() {
        okHttpClient = OkHttpClient()
            .newBuilder()
            .build()
    }

}

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data)

    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)

    class Loading<T> : NetworkResult<T>()

}
