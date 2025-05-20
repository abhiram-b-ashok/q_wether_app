package com.example.qweather.data.models.weather_news

import com.example.qweather.data.network.ApiResponse
import org.json.JSONObject



data class ResponseData(
    val status: Boolean,
    val result: ResultData?
)

data class ResultData(
    val list: List<MonthlyData>
)

data class MonthlyData(
    val year: String,
    val month: String,
    val data: List<WeatherNewsModel>
)

data class WeatherNewsModel(
    val image: String,
    val title: String,
    val date: String,
)


fun JSONObject.toAPIResponse(): ApiResponse {
    return ApiResponse(
        code = this.getInt("code"),
        exception = null,
        message = this.getString("message"),
        response = this.getJSONObject("response").toResponseData(),
    )
}

fun JSONObject.toResponseData(): ResponseData {
    return ResponseData(
        status = this.getBoolean("status"),
        result = this.getJSONObject("result").toResultData()
    )
}

fun JSONObject.toResultData(): ResultData {
    val listArray = this.getJSONArray("list")
    val monthlyDataList = mutableListOf<MonthlyData>()
    for (i in 0 until listArray.length()) {
        monthlyDataList.add(listArray.getJSONObject(i).toMonthlyData())
    }
    return ResultData(
        list = monthlyDataList
    )
}

fun JSONObject.toMonthlyData(): MonthlyData {
    val dataArray = this.getJSONArray("data")
    val weatherNewsList = mutableListOf<WeatherNewsModel>()
    for (i in 0 until dataArray.length()) {
        weatherNewsList.add(dataArray.getJSONObject(i).toWeatherNewsModel())
    }
    return MonthlyData(
        year = this.getString("year"),
        month = this.getString("month"),
        data = weatherNewsList
    )
}

fun JSONObject.toWeatherNewsModel(): WeatherNewsModel {
    return WeatherNewsModel(

        title = this.getString("title"),
        image = this.getString("image"),
        date = this.getString("date"),
    )
}






