package com.example.qweather.ui.dashboard.inner_fragments.weather_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentMapBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragmentDirections


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webViewCard.setOnClickListener {
            findNavController().navigate(DefaultDashboardFragmentDirections.actionDefaultDashboardFragmentToWeatherMapFragment())
        }

    }


}