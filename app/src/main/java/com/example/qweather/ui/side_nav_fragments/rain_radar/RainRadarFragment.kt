package com.example.qweather.ui.side_nav_fragments.rain_radar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentRainRadarBinding


class RainRadarFragment : Fragment() {
    private lateinit var binding: FragmentRainRadarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRainRadarBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageWebView.setOnClickListener {
            findNavController().navigate(R.id.action_rainRadarFragment_to_radarWebViewFragment)
        }


    }



}