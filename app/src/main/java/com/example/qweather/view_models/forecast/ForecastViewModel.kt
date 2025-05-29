package com.example.qweather.view_models.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.forecast.ForecastResult
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.ForecastRepository

class ForecastViewModel(private val repository: ForecastRepository): ViewModel() {

    private val _forecastResult = MutableLiveData<ForecastResult?>()
    val forecastResult: LiveData<ForecastResult?> get() = _forecastResult

    fun getForecast(cityId: Int) {
        repository.fetchForecastData(cityId) { result ->
            when (result) {
                is NetworkResult.Success -> _forecastResult.postValue(result.data)
                is NetworkResult.Error -> Log.e(
                    "ForecastViewModel",
                    result.message ?: "Unknown error"
                )

                else -> Unit
            }

        }

    }
}