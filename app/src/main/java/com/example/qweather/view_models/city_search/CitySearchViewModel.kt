package com.example.qweather.view_models.city_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.city_search.CitySearchApiResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitySearchRepository
import kotlinx.coroutines.launch

class CitySearchViewModel(private val citySearchRepository: CitySearchRepository) : ViewModel() {
    private val _citySearchResponse = MutableLiveData<NetworkResult<CitySearchApiResponse>>()
    val citySearchResponse: LiveData<NetworkResult<CitySearchApiResponse>> = _citySearchResponse

    fun searchCities(inputText: String) {
        viewModelScope.launch {
            try {
                val response = citySearchRepository.searchCities(inputText)
                _citySearchResponse.value = NetworkResult.Success(response) as NetworkResult<CitySearchApiResponse>?
            } catch (e: Exception) {
                _citySearchResponse.value = NetworkResult.Error(e.toString())
            }
        }
    }
}