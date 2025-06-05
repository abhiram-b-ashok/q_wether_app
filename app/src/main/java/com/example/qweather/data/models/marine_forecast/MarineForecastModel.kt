package com.example.qweather.data.models.marine_forecast

import org.json.JSONArray
import org.json.JSONObject


data class MarineForecastApiResponse(
    val status: Boolean,
    val result: MarineForecastResult?
)

data class MarineForecastResult(
    val marineForecast: MarineForecastData
)
data class MarineForecastData(
    val data: List<MarineForecast>
)

data class MarineForecast(
    val DataDateTime: String,
    val FTemperature: String,
    val FWindSpeed: String,
    val FWindDirectionText: String,
    val FVisibility: String,
    val FCurrentSpeed : String,
    val FSeaStateText : String,
    val FWarningText : String,

)

fun JSONObject.toMarineForecastApiResponse(): MarineForecastApiResponse {
    val responseObject = this.getJSONObject("Response")
    return MarineForecastApiResponse(
        status = responseObject.getBoolean("status"),
        result = responseObject.optJSONObject("result")?.toMarineForeCastResult()
    )
}

fun JSONObject.toMarineForeCastResult(): MarineForecastResult {
    return MarineForecastResult(
        marineForecast = this.getJSONArray("marine_forecast").toMarineForecastData()
    )
}

fun JSONArray.toMarineForecastData(): MarineForecastData {
    val marineForecastList = mutableListOf<MarineForecast>()
    for (i in 0 until this.length()) {
        val obj = this.getJSONObject(i)
        marineForecastList.add(
            MarineForecast(
                DataDateTime = obj.getString("DataDateTime"),
                FTemperature = obj.getString("FTemperature"),
                FWindSpeed = obj.getString("FWindSpeed"),
                FWindDirectionText = obj.getString("FWindDirectionText"),
                FVisibility = obj.getString("FVisibility"),
                FCurrentSpeed = obj.getString("FCurrentSpeed"),
                FSeaStateText = obj.getString("FSeaStateText"),
                FWarningText = obj.getString("FWarningText"),
            )
        )
    }
    return MarineForecastData(
        data = marineForecastList
    )
}

