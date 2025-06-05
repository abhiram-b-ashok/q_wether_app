package com.example.qweather.ui.dashboard.inner_fragments.marine_forecast.marine_forecast_detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentMarineForCastDeatiledBinding
import com.example.qweather.ui.dashboard.inner_fragments.marine_forecast.marine_forecast_detailed.marine_bottomsheet.MarineBottomSheetFragment


class MarineForCastDeatiledFragment : Fragment() {
    private lateinit var binding: FragmentMarineForCastDeatiledBinding
    private lateinit var bottomSheetFragment: MarineBottomSheetFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarineForCastDeatiledBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetFragment = MarineBottomSheetFragment()

        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

        binding.imageOfMarineForecast.setOnClickListener {
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

        }


    }


}