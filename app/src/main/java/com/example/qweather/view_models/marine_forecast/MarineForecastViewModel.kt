package com.example.qweather.view_models.marine_forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.marine_forecast.MarineForecast
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.MarineForecastRepository
import kotlinx.coroutines.launch

class MarineForecastViewModel(private val repository: MarineForecastRepository) : ViewModel() {
    private val _marineForecastList = MutableLiveData<NetworkResult<List<MarineForecast>>>()
    val marineForecastList: LiveData<NetworkResult<List<MarineForecast>>> = _marineForecastList
     fun getMarineForecast()
    {
        _marineForecastList.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = repository.getMarineForecast()
            _marineForecastList.value = result
        }
    }

}