package com.example.qweather.data.models.city_search

import org.json.JSONObject

data class CitySearchModel(
    val name: String,
    val country: String,
    val cityId: Int,
    val lat: Double,
    val lon: Double,
)
data class CitySearchApiResponse(
    val status: Boolean,
    val result: CitySearchResult?
)
data class CitySearchResult(
    val cities: CitySearchData
)

data class CitySearchData(
    val data: List<CitySearchModel>
)


fun JSONObject.toCitySearchApiResponse(): CitySearchApiResponse {
    val responseObject = this.getJSONObject("Response")
    return CitySearchApiResponse(
        status = responseObject.getBoolean("status"),
        result = responseObject.optJSONObject("result")?.toCitySearchResult()
    )
}

fun JSONObject.toCitySearchResult(): CitySearchResult {
    return CitySearchResult(
        cities = this.getJSONObject("cities").toCitySearchData()
    )
}


fun JSONObject.toCitySearchData(): CitySearchData {
    val dataArray = this.getJSONArray("world")
    val citiesList = mutableListOf<CitySearchModel>()
    for (i in 0 until dataArray.length()) {
        val obj = dataArray.getJSONObject(i)
        citiesList.add(
            CitySearchModel(
                 cityId = obj.getInt("city_id"),
                country = obj.getString("country_name"),
                name = obj.getString("name"),
                lat = obj.getDouble("latitude"),
                lon = obj.getDouble("longitude"),
                )
        )
    }
    return CitySearchData(
        data = citiesList
    )
}

