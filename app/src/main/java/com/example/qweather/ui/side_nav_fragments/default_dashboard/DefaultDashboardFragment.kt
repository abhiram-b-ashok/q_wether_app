package com.example.qweather.ui.side_nav_fragments.default_dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDefaultDashboardBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.CityBottomSheetFragment


class DefaultDashboardFragment : Fragment() {
    private lateinit var binding: FragmentDefaultDashboardBinding
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }
    private val defaultFragmentContainers = setOf(
        R.id.current_weather_fragment_container,
        R.id.forecast_fragment_container,
        R.id.sun_rise_set_fragment_container,
        R.id.moon_phase_fragment_container,
        R.id.tide_fragment_container,
        R.id.weather_map_container,
    )
    private val qatarOnlyFragmentContainers = setOf(
        R.id.warning_fragment_container,
        R.id.seasonal_fragment_container,
        R.id.radar_fragment_container,
    )
    private val allFragmentContainerIds = defaultFragmentContainers + qatarOnlyFragmentContainers


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefaultDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCitySelection()
        loadSavedCity()
        binding.dashboardSettingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_defaultDashboardFragment_to_settingsFragment)
        }

        view.post {
            applyFragmentContainerVisibility()
        }

        Log.e("@@@@@latitude", "${Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))}")
        Log.e(
            "@@@@@longitude",
            "${Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))}"
        )


    }


    private fun setupCitySelection() {
        binding.locationSelector.apply {
            setOnClickListener { showCityBottomSheet() }
            text = sharedPrefs.getString("LAST_SELECTED_CITY", getString(R.string.select_city))
        }
    }

    private fun showCityBottomSheet() {
        CityBottomSheetFragment().show(childFragmentManager, "CityBottomSheet")
    }

    override fun onStart() {
        super.onStart()
        setupResultListener()
    }

    private fun setupResultListener() {
        childFragmentManager.setFragmentResultListener(
            "CITY_SELECTION_RESULT",
            viewLifecycleOwner
        ) { _, bundle ->
            handleCitySelection(
                bundle.getString("SELECTED_CITY") ?: return@setFragmentResultListener,
                bundle.getBoolean("IS_QATAR", true),
                bundle.getDouble("LATITUDE"),
                bundle.getDouble("LONGITUDE")
            )
        }
    }

    private fun handleCitySelection(
        cityName: String,
        isQatar: Boolean,
        latitude: Double,
        longitude: Double
    ) {
        binding.locationSelector.text = cityName

        with(sharedPrefs.edit()) {
            putString("LAST_SELECTED_CITY", cityName)
            putBoolean("LAST_CITY_IS_QATAR", isQatar)
            putLong("LAST_CITY_LATITUDE", java.lang.Double.doubleToRawLongBits(latitude))
            putLong("LAST_CITY_LONGITUDE", java.lang.Double.doubleToRawLongBits(longitude))
            apply()
        }

        val currentDestinationId = findNavController().currentDestination?.id
        if (currentDestinationId == R.id.defaultDashboardFragment) {
            findNavController().navigate(R.id.action_defaultDashboardFragment_to_defaultDashboardFragment)
        }
    }

    private fun loadSavedCity() {
        binding.locationSelector.text = sharedPrefs.getString(
            "LAST_SELECTED_CITY",
            getString(R.string.select_city)
        )
    }

    private fun applyFragmentContainerVisibility() {
        val isQatar = sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)

        for (id in allFragmentContainerIds) {
            val containerView = binding.root.findViewById<View>(id)

            containerView?.let {
                if (defaultFragmentContainers.contains(id)) {
                    it.visibility = View.VISIBLE
                } else if (qatarOnlyFragmentContainers.contains(id)) {
                    if (isQatar) {
                        it.visibility = View.VISIBLE
                    } else {
                        it.visibility = View.GONE
                    }
                }
            }
        }
    }

}