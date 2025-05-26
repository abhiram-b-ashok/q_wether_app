package com.example.qweather.data.models.current_weather

data class CurrentWeatherModel(
    val time: Long,
    val temperature: Double,
    val temperatureUnit: String,
    val weatherType: String,
    val weatherIcon: String,
    val humidity: Int,
    val humidityUnit: String,
    val windPower: Double,
    val windPowerUnit: String,
    val windDirection: Int,
    val windDirectionText: String,
    val visibility: Int,
    val visibilityUnit: String,
    val sunrise: Long,
    val sunset: Long,
    val feelsLike: Double,
    val feelsLikeUnit: String,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val clouds: Int,
    val pressure: Int,
    val pressureUnit: String,
    val rain: Int,
    val rainUnit: String,
    val uvIndex: Double
)