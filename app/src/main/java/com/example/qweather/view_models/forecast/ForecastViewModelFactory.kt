package com.example.qweather.view_models.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.ForecastRepository

class ForecastViewModelFactory(
    private val repository: ForecastRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForecastViewModel(repository) as T
    }
}
