package com.example.qweather.ui.dashboard.inner_fragments.current_weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentCurrentWeatherBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_details_weather_model.WeatherViewModel
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentWeatherFragment : Fragment() {
    private lateinit var binding: FragmentCurrentWeatherBinding
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WeatherVM", "CurrentWeather VM hash: ${weatherViewModel.hashCode()}")

        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.currentWeather?.let { current ->
                Log.d("WeatherVM", "Current Weather: $current")


                    binding.apply {
                        cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")
                        val timeStamp = current.time
                        cityTime.text = convertTimestampToTime(timeStamp)
                        cityTemperature.text = current.temperature.toString()
                        cityWeatherCondition.text = current.weather_type
                        feelsLikTemp.text = current.feels_like.toString()
                        humiPercent.text = current.humidity.toString()
                        windDirection.text = current.wind_direction.toString()
                        windSpeed.text = current.wind_power.toString()
                        windSpeedUnit.text = current.wind_power_unit.toString()
                        temperatureUnit.text = current.temperature_unit.toString()
                        tempUp.text = current.temperature_max.toString()
                        tempDown.text = current.temperature_min.toString()

                        if (current.weather_type == "Clear") {
                            currentWeatherLayout.setBackgroundResource(R.drawable.clear_sky_day)
                        }
                        else if (current.weather_type == "Dust") {
                            currentWeatherLayout.setBackgroundResource(R.drawable.dusty_day)
                        }
                        else if (current.weather_type == "Rain") {
                            currentWeatherLayout.setBackgroundResource(R.drawable.rainy)
                        }
                        else if (current.weather_type == "Stormy") {
                            currentWeatherLayout.setBackgroundResource(R.drawable.thunderstorm)
                        }
                        else if (current.weather_type == "Snow") {
                            currentWeatherLayout.setBackgroundResource(R.drawable.snowy)
                        }
                        else
                        {
                            currentWeatherLayout.setBackgroundResource(R.drawable.clear_sky_day)
                        }

                    }
                }

            }

        }
    private fun convertTimestampToTime(timestamp: Long, pattern: String =  "dd MMMM yyyy, HH:mm a"): String {
        val date = Date(timestamp *1000L)
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        return format.format(date)
    }
    }


