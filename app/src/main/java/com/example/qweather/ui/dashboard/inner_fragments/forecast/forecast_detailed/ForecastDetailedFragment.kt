package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.data.models.cities_weather.HourlyForecast
import com.example.qweather.databinding.FragmentForecastDetailedBinding
import com.example.qweather.repository.WeatherRepository
import com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter.ForeCastHourlyAdapter
import com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter.ForecastDailyAdapter
import com.example.qweather.view_models.city_weather.WeatherViewModel
import com.example.qweather.view_models.city_weather.WeatherViewModelFactory



class ForecastDetailedFragment : Fragment() {
    private lateinit var binding: FragmentForecastDetailedBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var dailyAdapter: ForecastDailyAdapter
    private lateinit var hourlyAdapter: ForeCastHourlyAdapter
    private lateinit var weatherViewModel: WeatherViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastDetailedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastRepository = WeatherRepository()
        val viewModelFactory = WeatherViewModelFactory(forecastRepository)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        val lat = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))
        val lon = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))
        val isQatar = sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)
        weatherViewModel.loadWeather(lat,lon,isQatar)

        binding.apply {
            cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")
            temp.text = "${Double.fromBits(sharedPrefs.getLong("LAST_TEMPERATURE", 0L))}°C"
            tempDown.text = "${Double.fromBits(sharedPrefs.getLong("LAST_TEMP_MIN", 0L))}°C"
            tempUp.text = "${Double.fromBits(sharedPrefs.getLong("LAST_TEMP_MAX", 0L))}°C"
            condition.text = sharedPrefs.getString("LAST_WEATHER_TYPE", "Clear")
            approx.text =
                "Feels like ${Double.fromBits(sharedPrefs.getLong("LAST_FEELS_LIKE", 0L))}°C"
            humiPercent.text = Double.fromBits(sharedPrefs.getLong("LAST_HUMIDITY", 0L)).toString()
            windSpeed.text = Double.fromBits(sharedPrefs.getLong("LAST_WIND_SPEED", 0L)).toString()
            date.text = sharedPrefs.getString("LAST_DATE", "0.0")
            windDirection.text = sharedPrefs.getString("LAST_WIND_DIRECTION", "N")
        }

        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.dailyForecast?.let { forecast ->
                Log.d("ForecastDetailedFragment", "Daily Forecast: $forecast")
                dailyAdapter = ForecastDailyAdapter(forecast)
                binding.dailyRecyclerView.adapter = dailyAdapter
                binding.rainValue.text = forecast[0].rain.toString()
                binding.pressureValue.text = forecast[0].pressure.toString()

            }
            result?.hourlyForecast?.let { forecast ->
                val allHourlyForecasts: List<HourlyForecast> = forecast.flatMap { hourlyWeather ->
                    hourlyWeather.dayDetails
                }
                Log.d("ForecastDetailedFragment", "Hourly Forecast: $allHourlyForecasts")

                hourlyAdapter = ForeCastHourlyAdapter(allHourlyForecasts)
                binding.hourlyRecyclerView.adapter = hourlyAdapter
            }
        }


    }
}


