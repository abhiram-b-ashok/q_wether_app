package com.example.qweather.view_models.marine_forecast


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.MarineForecastRepository

class MarineForecastViewModelFactory(
    private val repository: MarineForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarineForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MarineForecastViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}