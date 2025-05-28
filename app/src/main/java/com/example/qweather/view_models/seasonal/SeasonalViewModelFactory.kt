package com.example.qweather.view_models.seasonal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.SeasonalRepository

class SeasonalViewModelFactory(private val repository: SeasonalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeasonalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SeasonalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}