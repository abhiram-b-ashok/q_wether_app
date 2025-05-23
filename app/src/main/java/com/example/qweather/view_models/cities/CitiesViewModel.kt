package com.example.qweather.view_models.cities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.cities.CitiesApiResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CitiesRepository) : ViewModel() {

    private val _citiesLiveData = MutableLiveData<NetworkResult<CitiesApiResponse>>()
    val citiesLiveData: LiveData<NetworkResult<CitiesApiResponse>> = _citiesLiveData

    fun fetchCities() {
        viewModelScope.launch {
            _citiesLiveData.value = NetworkResult.Loading()
            val result = repository.getCities()
            _citiesLiveData.value = result
        }
    }

}



