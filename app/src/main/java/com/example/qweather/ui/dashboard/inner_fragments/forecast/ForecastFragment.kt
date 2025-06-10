package com.example.qweather.ui.dashboard.inner_fragments.forecast

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.databinding.FragmentForecastBinding
import com.example.qweather.ui.dashboard.inner_fragments.forecast.adapter.ForecastAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragmentDirections
import com.example.qweather.view_models.city_weather.WeatherViewModel


class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewDetailsButton.setOnClickListener {
            findNavController().navigate(DefaultDashboardFragmentDirections.actionDefaultDashboardFragmentToForecastDetailedFragment())

        }
        binding.layout.setOnClickListener {
            findNavController().navigate(DefaultDashboardFragmentDirections.actionDefaultDashboardFragmentToForecastDetailedFragment())
        }

        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.dailyForecast?.let { forecast ->
                binding.dailyRecyclerView.adapter = ForecastAdapter(forecast)
            }
            result?.hourlyForecast?.let { forecast ->
                Log.d("ForecastFragment", "Hourly Forecast: $forecast")
            }

        }


    }


}