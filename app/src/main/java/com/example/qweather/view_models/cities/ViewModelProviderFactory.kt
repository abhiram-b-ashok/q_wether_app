package com.example.qweather.view_models.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.CitiesRepository

class CityViewModelFactory(private val repository: CitiesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    companion object {
        const val REQUEST_KEY = "city_selection_request"
        const val KEY_CITY_NAME = "selected_city"
        const val KEY_IS_QATAR = "is_qatar"
    }


}