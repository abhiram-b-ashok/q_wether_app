package com.example.qweather.data.network

import com.example.qweather.data.models.weather_news.ResponseData

data class ApiResponse(
    val code: Int? = null,
    val exception: Throwable? = null,
    val message: String? = null,
    val response: ResponseData
)


