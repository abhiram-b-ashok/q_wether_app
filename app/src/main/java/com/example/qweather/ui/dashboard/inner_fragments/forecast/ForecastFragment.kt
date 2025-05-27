package com.example.qweather.ui.dashboard.inner_fragments.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentForecastBinding
import com.example.qweather.ui.dashboard.inner_fragments.forecast.adapter.ForecastAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_details_weather_model.WeatherViewModel


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

        }

        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.dailyForecast?.let { forecast ->
                binding.dailyRecyclerView.adapter = ForecastAdapter(forecast)
            }
        }

    }


}