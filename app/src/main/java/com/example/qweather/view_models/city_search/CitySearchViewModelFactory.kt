package com.example.qweather.view_models.city_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.CitySearchRepository

class CitySearchViewModelFactory(private val citySearchRepository: CitySearchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitySearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CitySearchViewModel(citySearchRepository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}