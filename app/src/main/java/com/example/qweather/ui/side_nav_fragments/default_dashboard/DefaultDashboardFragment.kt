package com.example.qweather.ui.side_nav_fragments.default_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qweather.databinding.FragmentDefaultDashboardBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.CityBottomSheetFragment

class DefaultDashboardFragment : Fragment() {
    private lateinit var binding: FragmentDefaultDashboardBinding
    private lateinit var cityBottomSheetFragment: CityBottomSheetFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultDashboardBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationSelector.setOnClickListener {
            cityBottomSheetFragment = CityBottomSheetFragment()
            cityBottomSheetFragment.show(childFragmentManager, "CityBottomSheetFragment")
        }


        childFragmentManager.setFragmentResultListener("citySelectionKey", viewLifecycleOwner) { requestKey, bundle ->
            val selectedCity = bundle.getString("selectedCity")
            if (selectedCity != null) {
                binding.locationSelector.text = selectedCity
            }
        }
    }


}