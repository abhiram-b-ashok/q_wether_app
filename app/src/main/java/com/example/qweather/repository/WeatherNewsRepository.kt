//package com.example.qweather.repository
//
//import com.example.qweather.data.models.weather_news.WeatherNewsModel
//import com.example.qweather.data.network.NetworkResult
//import com.example.qweather.data.network.api_call.getWeatherApi
//
//
////class WeatherNewsRepository {
////    suspend fun fetchWeatherNews(): NetworkResult<List<WeatherNewsModel>> {
////        return try {
////            val apiResponse = getWeatherApi()
////            if (apiResponse.code == 200 && apiResponse.response.status) {
////                val allNews = apiResponse.response.result?.list?.flatMap { it.data } ?: emptyList()
////                NetworkResult.Success(allNews)
////            } else {
////                NetworkResult.Error(apiResponse.message)
////            }
////        } catch (e: Exception) {
////            NetworkResult.Error(e.message)
////        }
////    }
////}
