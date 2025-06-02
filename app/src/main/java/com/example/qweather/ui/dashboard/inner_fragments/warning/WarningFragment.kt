package com.example.qweather.ui.dashboard.inner_fragments.warning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.databinding.FragmentWarningBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_weather.WeatherViewModel


class WarningFragment : Fragment() {
    private lateinit var binding: FragmentWarningBinding
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWarningBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.hourlyForecast?.let { hourlyForecast ->
                binding.apply {
                    val hourlyForecast = hourlyForecast[0].dayDetails
                    warningValue.text = hourlyForecast[0].warning_text
                    visibleValue.text = "${hourlyForecast[0].visibility} ${hourlyForecast[0].visibility_unit}"
                    windValue.text = "${hourlyForecast[0].wind_power} ${hourlyForecast[0].wind_power_unit}"
                    tempValue.text = "${hourlyForecast[0].temperature}${hourlyForecast[0].temperature_unit}"

                }


            }


        }
    }
}