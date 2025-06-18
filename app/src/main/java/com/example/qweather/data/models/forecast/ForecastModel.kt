package com.example.qweather.data.models.forecast

import com.google.gson.annotations.SerializedName

data class ForecastResponseWrapper(
    @SerializedName("Response") val response: ForecastResponseContent
)

data class ForecastResponseContent(
    val status: Boolean,
    val result: ForecastResult
)

data class ForecastResult(
    @SerializedName("daily_weather") val dailyForecast: List<DailyWeatherModel>?,
)

data class DailyWeatherModel(
    val date: String,
    val temperature: Double,
    val temperature_min: Double,
    val temperature_max: Double,
    val feels_like_day: Double,
    val humidity: Int,
    val pressure: Int,
    val rain: Double,
    val clouds: Int,
    val wind_speed: Double,
    val weather_type: String,
    val weather_icon: String,
    val temperature_unit: String,
    val rain_unit: String,
    val humidity_unit: String
)

data class SavedForecastModel(
    val cityId: Int,
    val cityName: String,
    val date: String,
    val temperature: Double,
    val temperatureUnit: String,
    val weatherType: String
)

