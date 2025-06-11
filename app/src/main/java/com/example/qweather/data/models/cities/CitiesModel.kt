package com.example.qweather.data.models.cities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class CitiesResponse(
    @Json(name = "Response")
    val response: ResponseData
)
@JsonClass(generateAdapter = true)
data class ResponseData(
    @Json(name = "status")
    val status: Boolean,
    @Json(name = "result")
    val result: ResultData
)
@JsonClass(generateAdapter = true)
data class ResultData(
    @Json(name = "cities")
    val cities: CitiesData
)
@JsonClass(generateAdapter = true)
data class CitiesData(
    @Json(name = "qatar")
    val qatar: List<QatarCityData>,
    @Json(name = "world")
    val world: List<WorldCityData>
)
@JsonClass(generateAdapter = true)
data class QatarCityData(
    @Json(name = "city_id")
    val cityId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "country_name")
    val countryName: String,
    @Json(name = "country_name_ar")
    val countryNameAr: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "utc_offset")
    val utcOffset: String,
    @Json(name = "name_ar")
    val nameAr: String
)
@JsonClass(generateAdapter = true)
data class WorldCityData(
    @Json(name = "city_id")
    val cityId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "country_name")
    val countryName: String,
    @Json(name = "country_name_ar")
    val countryNameAr: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "utc_offset")
    val utcOffset: String,
    @Json(name = "name_ar")
    val nameAr: String

)


