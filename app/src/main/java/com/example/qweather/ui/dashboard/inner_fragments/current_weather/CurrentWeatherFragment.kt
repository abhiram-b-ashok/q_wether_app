package com.example.qweather.ui.dashboard.inner_fragments.current_weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.R
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentCurrentWeatherBinding
import com.example.qweather.repository.WeatherRepository
import com.example.qweather.repository.WeatherRepository.WeatherRepositoryProvider
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_details_weather_model.WeatherViewModel
import com.example.qweather.view_models.city_details_weather_model.WeatherViewModelFactory


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
                        cityTime.text = current.time.toString()
                        cityTemperature.text = current.temperature.toString()
                        cityWeatherCondition.text = current.weather_type
                        feelsLikTemp.text = current.feels_like.toString()

                    }
                }

            }
        }
    }


