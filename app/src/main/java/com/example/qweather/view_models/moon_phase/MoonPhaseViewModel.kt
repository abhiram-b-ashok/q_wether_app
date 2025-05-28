package com.example.qweather.view_models.moon_phase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.moon_phase.MoonPhaseModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.MoonPhaseRepository

class MoonPhaseViewModel(private val repository: MoonPhaseRepository) : ViewModel() {

    private val _moonPhase = MutableLiveData<NetworkResult<MoonPhaseModel>>()
    val moonPhase: LiveData<NetworkResult<MoonPhaseModel>> get() = _moonPhase

    fun loadMoonPhase(cityId: Int) {
        repository.fetchMoonPhaseData(cityId) { result ->
            _moonPhase.postValue(result)
        }
    }
}
