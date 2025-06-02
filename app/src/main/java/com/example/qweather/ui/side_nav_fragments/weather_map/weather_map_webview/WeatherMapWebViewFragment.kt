package com.example.qweather.ui.side_nav_fragments.weather_map.weather_map_webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentWeatherMapWebViewBinding


class WeatherMapWebViewFragment : Fragment() {
    private lateinit var binding: FragmentWeatherMapWebViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherMapWebViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView
        webView.loadUrl("https://www.windy.com/-Waves-waves?waves,9.918,76.256,5")

    }


}