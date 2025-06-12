package com.example.qweather.repository

import com.example.qweather.data.models.city_by_location.CityWeatherApiResponse
import com.example.qweather.data.models.city_search.CitySearchModel
import com.example.qweather.data.network.NetworkHandler
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.getSearchCities

//class CitySearchRepository {
//        suspend fun searchCities(inputText: String): NetworkResult<List<CitySearchModel>> {
//            return try {
//                val apiResponse = getSearchCities(inputText)
//                if (apiResponse.status) {
//                    val cities = apiResponse.result?.cities?.data ?: emptyList()
//                    NetworkResult.Success(cities)
//                } else {
//                    NetworkResult.Error("API returned failure status")
//                }
//            } catch (e: Exception) {
//                NetworkResult.Error(e.message ?: "Unknown error")
//            }
//        }
//    }

class CitySearchRepository {
    suspend fun searchCities(inputText: String): NetworkResult<List<CitySearchModel>> {
        return try {
            val apiResponse = getSearchCities(inputText)
            if (apiResponse.status) {
                val cities = apiResponse.result?.cities?.data ?: emptyList()
                NetworkResult.Success(cities)
            } else {
                NetworkResult.Error("API returned failure status")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}
