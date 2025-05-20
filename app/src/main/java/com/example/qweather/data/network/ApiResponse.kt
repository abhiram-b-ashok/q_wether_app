package com.example.qweather.data.network

import com.example.qweather.data.models.weather_news.ResponseData

data class ApiResponse(
    val code: Int?,
    val exception: Throwable?,
    val message: String?,
    val response: ResponseData
)