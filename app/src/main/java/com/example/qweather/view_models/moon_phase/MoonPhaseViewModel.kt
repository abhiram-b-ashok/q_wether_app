package com.example.qweather.view_models.moon_phase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.moon_phase.MoonPhaseModel

class MoonPhaseViewModel():ViewModel()  {
    private val _moonPhase = MutableLiveData<MoonPhaseModel>()
    val moonPhase: LiveData<MoonPhaseModel> get() = _moonPhase
}