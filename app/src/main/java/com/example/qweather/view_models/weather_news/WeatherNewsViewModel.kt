//package com.example.qweather.view_models.weather_news
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.qweather.data.models.weather_news.WeatherNewsModel
//import com.example.qweather.data.network.NetworkResult
//import kotlinx.coroutines.launch
//
//class WeatherNewsViewModel(private val weatherNewsRepository: WeatherNewsRepository) : ViewModel() {
//    private val _weatherNewsList = MutableLiveData<NetworkResult<List<WeatherNewsModel>>>()
//    val weatherNewsList: LiveData<NetworkResult<List<WeatherNewsModel>>> = _weatherNewsList
//
//    fun getWeatherNews() {
//        viewModelScope.launch {
//            _weatherNewsList.value = NetworkResult.Loading()
//            val result = weatherNewsRepository.fetchWeatherNews()
//            _weatherNewsList.value = result
//        }
//    }
//}
