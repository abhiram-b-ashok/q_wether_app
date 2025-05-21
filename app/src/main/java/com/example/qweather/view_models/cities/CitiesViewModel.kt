package com.example.qweather.view_models.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.cities.CitiesApiResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.fetchCities
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import kotlinx.coroutines.launch

class CitiesViewModel(private val repository: CitiesRepository) : ViewModel() {

    private val _citiesResult = MutableLiveData<NetworkResult<CitiesApiResponse>>()
    val citiesResult: LiveData<NetworkResult<CitiesApiResponse>> get() = _citiesResult

    fun fetchCities() {
        viewModelScope.launch {
            _citiesResult.postValue(NetworkResult.Loading())
            val result = repository.getCities()
            _citiesResult.postValue(result)
        }
    }
}

