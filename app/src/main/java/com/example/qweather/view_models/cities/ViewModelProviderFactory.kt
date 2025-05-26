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



}