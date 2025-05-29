package com.example.qweather.data.models.cities_weather

import com.google.gson.annotations.SerializedName


data class CurrentWeather(
    val time: Long,
    val temperature: Double,
    val temperature_unit: String,
    val weather_type: String,
    val humidity: Int,
    val humidity_unit: String,
    val wind_power: Double,
    val wind_power_unit: String,
    val wind_direction: Int,
    val visibility: Int,
    val visibility_unit: String,
    val sunrise: Long,
    val sunset: Long,
    val feels_like: Double,
    val pressure: Int,
    val temperature_min: Double,
    val temperature_max: Double,
    val pressure_unit: String,
    val rain: Double,
    val rain_unit: String,
    val clouds: Int,
    val uv_index: String,
    val weather_icon: String
)


data class DailyWeather(
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


data class WeatherResponseWrapper(
    @SerializedName("Response") val response: WeatherResponseContent
)

data class WeatherResponseContent(
    val status: Boolean,
    val result: WeatherResult
)

data class WeatherResult(
    @SerializedName("current_weather") val currentWeather: CurrentWeather?,
    @SerializedName("daily_weather") val dailyForecast: List<DailyWeather>?,
    @SerializedName("hourly_data") val hourlyForecast: List<HourlyWeather>?
)
data class HourlyWeather(
    val date: String,
    @SerializedName("day_details") val dayDetails: List<HourlyForecast>
)

data class HourlyForecast(
    val time: String,
    val temperature: Double,
    val temperature_unit: String,
    val weather_type: String,
    val humidity: Int,
    val humidity_unit: String,
    val wind_power: Double,
    val wind_direction_text: String,
    val wind_power_unit: String,
    val wind_direction: Int,
    val visibility: Int,
    val visibility_unit: String
)

