package com.example.qweather.view_models.city_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.city_search.CitySearchApiResponse
import com.example.qweather.data.models.city_search.CitySearchModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitySearchRepository
import com.example.qweather.repository.WeatherRepository.WeatherRepositoryProvider.repository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import kotlinx.coroutines.launch
//class CitySearchViewModel(private val citySearchRepository: CitySearchRepository) : ViewModel() {
//
//    private val _citySearchResponse = MutableLiveData<NetworkResult<List<CitySearchModel>>>()
//    val citySearchResponse: LiveData<NetworkResult<List<CitySearchModel>>> = _citySearchResponse
//
//    fun searchCities(inputText: String) {
//        viewModelScope.launch {
//            when (val response = citySearchRepository.searchCities(inputText)) {
//                is NetworkResult.Success -> {
//                    _citySearchResponse.value = response
//                }
//                is NetworkResult.Error -> {
//                    _citySearchResponse.value = NetworkResult.Error(response.message ?: "Unknown error")
//                }
//                else -> {}
//            }
//        }
//    }
//}

class CitySearchViewModel(private val citySearchRepository: CitySearchRepository) : ViewModel() {

    private val _citySearchResponse = MutableLiveData<NetworkResult<List<CitySearchModel>>>()
    val citySearchResponse: LiveData<NetworkResult<List<CitySearchModel>>> = _citySearchResponse

    fun searchCities(inputText: String) {
        viewModelScope.launch {
            _citySearchResponse.value = NetworkResult.Loading() // show loading if needed
            val result = citySearchRepository.searchCities(inputText)
            _citySearchResponse.value = result
        }
    }
}
