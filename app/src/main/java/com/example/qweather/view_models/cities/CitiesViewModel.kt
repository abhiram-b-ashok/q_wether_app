package com.example.qweather.view_models.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitiesRepository
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CitiesRepository) : ViewModel() {
    private val _citiesLiveData = MutableLiveData<NetworkResult<CitiesResponse>>()
    val citiesLiveData: LiveData<NetworkResult<CitiesResponse>> = _citiesLiveData

    fun fetchCities() {
        viewModelScope.launch {
            _citiesLiveData.value = NetworkResult.Loading()
            _citiesLiveData.value = repository.getCities()
        }
    }
}



