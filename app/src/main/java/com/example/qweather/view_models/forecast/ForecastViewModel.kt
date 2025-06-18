package com.example.qweather.view_models.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.forecast.SavedForecastModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.repository.ForecastRepository
import java.util.concurrent.atomic.AtomicInteger

class ForecastViewModel(private val repository: ForecastRepository) : ViewModel() {

    private val _savedForecasts = MutableLiveData<List<SavedForecastModel>>()
    val savedForecasts: LiveData<List<SavedForecastModel>> = _savedForecasts

    fun loadForecastsForSavedCities(savedCities: List<FavoriteCitiesModel>) {
        val forecastList = mutableListOf<SavedForecastModel>()
        val pendingCount = AtomicInteger(savedCities.size)

        for (city in savedCities) {
            repository.fetchForecastData(city.cityId) { result ->
                if (result is NetworkResult.Success) {
                    val daily = result.data?.dailyForecast?.firstOrNull()
                    Log.d("ForecastViewModel", "Forecast data: ${result.data?.dailyForecast}")

                    if (daily != null) {
                        forecastList.add(
                            SavedForecastModel(
                                cityId = city.cityId,
                                cityName = city.cityName,
                                date = daily.date,
                                temperature = daily.temperature,
                                temperatureUnit = daily.temperature_unit,
                                weatherType = daily.weather_type
                            )
                        )
                    }
                }
                if (pendingCount.decrementAndGet() == 0) {
                    _savedForecasts.postValue(forecastList)
                }
            }
        }
    }
}
