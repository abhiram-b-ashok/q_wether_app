package com.example.qweather.data.models.forecast

data class ForecastModel(
    val temperature: Float,
    val temperature_min: Int,
    val temperature_max: Int,
    val temperature_unit: String,
    val date: String,
    val icon: Int
)
