package com.example.qweather.data.models.city_by_location

import org.json.JSONObject


data class CityWeatherApiResponse(
    val response: ResponseData
)
data class CityWeatherModel(
    val id: Int,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val temperature: Double,
    val temperatureUnit: String,
    val humidity: Double,
    val humidityUnit: String,
    val windSpeed: Double,
    val windSpeedUnit: String,
    val windDirection: String,
    val pressure: Double,
    val pressureUnit: String,
    val visibility: Double,
    val visibilityUnit: String,
    val description: String,
    val icon: String

)

data class ResponseData(
    val status: Boolean,
    val result: Result
)

data class Result(
    val list: List<CityWeatherModel>
)

fun JSONObject.toCityWeatherApiResponse(): CityWeatherApiResponse {
    val responseObj = this.getJSONObject("Response")
    val status = responseObj.getBoolean("status")
    val resultObj = responseObj.getJSONObject("result")
    val listJsonArray = resultObj.getJSONArray("list")

    val cities = mutableListOf<CityWeatherModel>()
    for (i in 0 until listJsonArray.length()) {
        val itemObj = listJsonArray.getJSONObject(i)
        val id = itemObj.getInt("id")
        val temperature = itemObj.getDouble("temperature")
        val temperatureUnit = itemObj.getString("temperatureUnit")
        val humidity = itemObj.getDouble("humidity")
        val humidityUnit = itemObj.getString("humidityUnit")
        val windSpeed = itemObj.getDouble("windSpeed")
        val windSpeedUnit = itemObj.getString("windSpeedUnit")
        val windDirection = itemObj.getString("windDirection")
        val pressure = itemObj.getDouble("pressure")
        val pressureUnit = itemObj.getString("pressureUnit")
        val visibility = itemObj.getDouble("visibility")
        val visibilityUnit = itemObj.getString("visibilityUnit")
        val description = itemObj.getString("description")
        val icon = itemObj.getString("icon")
        val name = itemObj.getString("name")
        val longitude = itemObj.getDouble("longitude")
        val latitude = itemObj.getDouble("latitude")

        cities.add(CityWeatherModel(id, name, longitude, latitude, temperature, temperatureUnit, humidity, humidityUnit, windSpeed, windSpeedUnit, windDirection, pressure, pressureUnit, visibility, visibilityUnit, description, icon,))

    }
    return CityWeatherApiResponse(
        response = ResponseData(
            status = status,
            result = Result(cities)
        ))

}
