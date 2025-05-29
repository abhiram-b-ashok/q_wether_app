package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentForecastDetailedBinding
import com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter.ForecastDailyAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_details_weather_model.WeatherViewModel


class ForecastDetailedFragment : Fragment() {
    private lateinit var binding: FragmentForecastDetailedBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var dailyAdapter: ForecastDailyAdapter
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }


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
            result?.hourlyForecast?.let { forecast ->
                dailyAdapter = ForecastDailyAdapter(forecast)
                binding.dailyRecyclerView.adapter = dailyAdapter
            }


    }
}


