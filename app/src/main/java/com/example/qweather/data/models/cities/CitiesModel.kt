package com.example.qweather.data.models.cities

import com.squareup.moshi.Json

data class CitiesApiResponse(
    @Json(name = "Response")
    val response: CitiesResponseWrapper
)


data class CitiesResponseWrapper(
    val status: Boolean,
    val result: CitiesResult
)

data class CitiesResult(
    val cities: CitiesByType
)

data class CitiesByType(
    val qatar: List<CityModel>,
    val world: List<CityModel>
)

data class CityModel(
    val city_id: Int,
    val name: String,
    val name_ar: String,
    val country: String,
    val country_name: String,
    val country_name_ar: String,
    val latitude: Double,
    val longitude: Double,
    val utc_offset: String
)

data class QatarCitiesModel(
    val cityName: String,
    var isSelected: Boolean = false
)

data class WorldCitiesModel(
    val cityName: String,
    var isSelected: Boolean = false
)
