package com.example.qweather.ui.side_nav_fragments.weather_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentWeatherMapBinding


class WeatherMapFragment : Fragment() {
    private lateinit var binding: FragmentWeatherMapBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView
        webView.loadUrl("https://www.weatherandradar.in/weather-map/doha/628970?center=25.14,50.4&placemark=25.2855,51.531&zoom=7.43&loop=true&tz=Asia%2FQatar")
    }


}