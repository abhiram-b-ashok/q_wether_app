package com.example.qweather.ui.side_nav_fragments.rain_radar.radar_web_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentRadarWebViewBinding


class RadarWebViewFragment : Fragment() {
    private lateinit var binding: FragmentRadarWebViewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRadarWebViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView
        webView.loadUrl("https://www.rainviewer.com/weather-radar-map-live.html")

    }


}