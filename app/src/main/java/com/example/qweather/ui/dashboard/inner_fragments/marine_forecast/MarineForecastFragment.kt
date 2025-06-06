package com.example.qweather.ui.dashboard.inner_fragments.marine_forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentMarineForecastBinding

class MarineForecastFragment : Fragment() {
    private lateinit var binding: FragmentMarineForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarineForecastBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.marineForecastLayOut.setOnClickListener {
            findNavController().navigate(R.id.action_defaultDashboardFragment_to_marineForCastDeatiledFragment)
        }

    }


}