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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.text.format


class ForecastDetailedFragment : Fragment() {
    private lateinit var binding: FragmentForecastDetailedBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var dailyAdapter: ForecastDailyAdapter
    private lateinit var hourlyAdapter: ForeCastHourlyAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    var dateDown = 0
    var dailyForecastSize = 0

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
        weatherViewModel =
            ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        val lat = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))
        val lon = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))
        val isQatar = sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)
        weatherViewModel.loadWeather(lat, lon, isQatar)

        binding.apply {
            cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")

            prevBt.setOnClickListener {

                if (dateDown > 0) {
                    dateDown--
                    weatherViewModel.loadWeather(lat, lon, isQatar)
                }
                else{
                    prevBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $dailyForecastSize is out of bounds")
                }


            }
            nextBt.setOnClickListener {
                if (dateDown < 4) {
                    dateDown++
                    weatherViewModel.loadWeather(lat, lon, isQatar)
                }
                else{
                    nextBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $dailyForecastSize is out of bounds")
                }


            }

            weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
                result?.dailyForecast?.let { forecast ->
                     dailyForecastSize = (forecast.size)
                    Log.d("ForecastDetailedFragment", "Daily Forecastsize: ${forecast.size}")
                    dailyAdapter = ForecastDailyAdapter(forecast)
                    dailyRecyclerView.adapter = dailyAdapter
                    dailyAdapter.notifyDataSetChanged()
                    rainValue.text = forecast[dateDown].rain.toString()
                    pressureValue.text = forecast[dateDown].pressure.toString()

                    temp.text = "${forecast[dateDown].temperature}°C"
                    tempDown.text = "${forecast[dateDown].temperature_min}°C"
                    tempUp.text = "${forecast[dateDown].temperature_max}°C"
                    condition.text = forecast[dateDown].weather_type
                    approx.text = "Feels like ${forecast[dateDown].feels_like_day}°C"
                    humiPercent.text = forecast[dateDown].humidity.toString()
                    windSpeed.text = forecast[dateDown].wind_speed.toString()
                    val originalDateString = forecast[dateDown].date

                    val inputFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a",Locale.ENGLISH)
                    val outputFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.ENGLISH)

                    try {
                        val dateTime1 = LocalDateTime.parse(originalDateString, inputFormatter)
                        val formattedDate = dateTime1.format(outputFormatter)
                        date.text = formattedDate
                        binding.dateMain.text = formattedDate
                    } catch (e: Exception) {
                        Log.e("ForecastDetailedFragment", "Error formatting date: ${e.message}")
                        date.text = originalDateString
                    }
                    windDirection.text = sharedPrefs.getString("LAST_WIND_DIRECTION", "N")
                }
                result?.hourlyForecast?.let { forecast ->
                    val allHourlyForecasts: List<HourlyForecast> = forecast.map { hourlyWeather ->
                        hourlyWeather.dayDetails[dateDown]
                    }
                    hourlyAdapter = ForeCastHourlyAdapter(allHourlyForecasts)
                    binding.hourlyRecyclerView.adapter = hourlyAdapter
                    hourlyAdapter.notifyDataSetChanged()

                }
            }
        }
    }
}


