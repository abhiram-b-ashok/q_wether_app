package com.example.qweather.view_models.moon_phase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.repository.MoonPhaseRepository

class MoonPhaseViewModelFactory(private val repository: MoonPhaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoonPhaseViewModel::class.java)) {
            return MoonPhaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
