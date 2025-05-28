package com.example.qweather.view_models.seasonal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.seasonal.SeasonalModel
import com.example.qweather.repository.SeasonalRepository



class SeasonalViewModel(private val repository: SeasonalRepository) : ViewModel() {
    private val _seasonalData = MutableLiveData<SeasonalModel?>()
    val seasonalData: LiveData<SeasonalModel?> get() = _seasonalData

    fun loadSeasonalData() {
        repository.fetchSeasonalData { result ->
            _seasonalData.postValue(result.data)
        }
    }
}