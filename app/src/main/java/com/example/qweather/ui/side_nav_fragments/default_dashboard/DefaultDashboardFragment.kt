package com.example.qweather.ui.side_nav_fragments.default_dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDefaultDashboardBinding
import com.example.qweather.repository.WeatherRepository.WeatherRepositoryProvider.repository
import com.example.qweather.ui.dashboard.inner_fragments.moon_phase.MoonPhaseFragment
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.CityBottomSheetFragment
import com.example.qweather.view_models.city_weather.WeatherViewModel
import com.example.qweather.view_models.city_weather.WeatherViewModelFactory


class DefaultDashboardFragment : Fragment() {
    private lateinit var binding: FragmentDefaultDashboardBinding
    internal lateinit var weatherViewModel: WeatherViewModel

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var visibilityPreferences: SharedPreferences

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
        R.id.marine_forecast_fragment_container
    )
    private val allFragmentContainerIds = defaultFragmentContainers + qatarOnlyFragmentContainers

    private val titleToContainerId = mapOf(
        "Current Weather" to R.id.current_weather_fragment_container,
        "Warning" to R.id.warning_fragment_container,
        "Forecast" to R.id.forecast_fragment_container,
        "Sunrise / Sunset info" to R.id.sun_rise_set_fragment_container,
        "Moon phase" to R.id.moon_phase_fragment_container,
        "Tidal information" to R.id.tide_fragment_container,
        "Rain Radar" to R.id.radar_fragment_container,
        "Weather Map" to R.id.weather_map_container,
        "Seasonal" to R.id.seasonal_fragment_container,
        "Marine Forecast" to R.id.marine_forecast_fragment_container
    )



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
        weatherViewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository)
        )[WeatherViewModel::class.java]
        setupCitySelection()
        loadSavedCity()
        binding.dashboardSettingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_defaultDashboardFragment_to_settingsFragment)
        }

        reorderDashboardFragments()


        view.post {
            applyFragmentContainerVisibility()
            hideFragment()
        }


        val lat = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))
        val lon = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))
        val isQatar = sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)
        val cityId = sharedPrefs.getInt("LAST_CITY_ID", 0)

        weatherViewModel.loadWeather(lat, lon, isQatar)
        val fragment = MoonPhaseFragment().apply {
            arguments = Bundle().apply {
                putInt("CITY_ID", cityId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.moon_phase_fragment_container, fragment)
            .commit()

        Log.e("@@@@@latitude", "${Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))}")
        Log.e(
            "@@@@@longitude",
            "${Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))}"
        )
        Log.d("@@@@CityId", "${sharedPrefs.getInt("LAST_CITY_ID", 0)}")
        Log.d("@@@@isQatar", "${sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)}")


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)

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
                bundle.getDouble("LONGITUDE"),
                bundle.getInt("CITY_ID"),
            )
        }
    }

    private fun handleCitySelection(
        cityName: String,
        isQatar: Boolean,
        latitude: Double,
        longitude: Double,
        cityId: Int
    ) {
        binding.locationSelector.text = cityName

        with(sharedPrefs.edit()) {
            putString("LAST_SELECTED_CITY", cityName)
            putBoolean("LAST_CITY_IS_QATAR", isQatar)
            putLong("LAST_CITY_LATITUDE", java.lang.Double.doubleToRawLongBits(latitude))
            putLong("LAST_CITY_LONGITUDE", java.lang.Double.doubleToRawLongBits(longitude))
            putInt("LAST_CITY_ID", cityId)
            apply()
        }

        val currentDestinationId = findNavController().currentDestination?.id
        if (currentDestinationId == R.id.defaultDashboardFragment) {
            findNavController().navigate(R.id.action_defaultDashboardFragment_to_defaultDashboardFragment)
        }
        weatherViewModel.loadWeather(latitude, longitude, isQatar)


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



    private fun reorderDashboardFragments() {
        val containerParent = binding.root.findViewById<LinearLayout>(R.id.dashboard_container_layout)
        val prefs = requireContext().getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
        val savedOrderString = prefs.getString("dashboardOrderString", null)

        if (!savedOrderString.isNullOrEmpty()) {
            val orderedTitles = savedOrderString.split(",")

            val orderedViews = orderedTitles.mapNotNull { title ->
                titleToContainerId[title]?.let { containerId ->
                    containerParent.findViewById<View>(containerId)
                }
            }

            val addButton = containerParent.findViewById<View>(R.id.dashboard_settings_button)
            containerParent.removeAllViews()

            // Add views in saved order
            orderedViews.forEach { containerParent.addView(it) }

            // Add the settings button back at the bottom
            containerParent.addView(addButton)
        }
    }


    private fun hideFragment() {
        visibilityPreferences = requireContext().getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
        val visibleTitles = visibilityPreferences.getStringSet("selectedDashboardItems", null)
        if (visibleTitles == null) {
            Log.w("DefaultDashboardFragment", "No visibility preferences found. All dashboard items might remain visible by default.")
            return
        }

        for (containerId in allFragmentContainerIds) {
            val containerView = binding.root.findViewById<View>(containerId)
            containerView?.let { view ->
                val titleForThisContainer = titleToContainerId.entries.find { it.value == containerId }?.key

                if (titleForThisContainer != null) {
                    if (!visibleTitles.contains(titleForThisContainer)) {
                        view.visibility = View.GONE
                        Log.d("DefaultDashboardFragment", "Hiding: $titleForThisContainer (ID: $containerId)")
                    } else {
                        view.visibility = View.VISIBLE
                        Log.d("DefaultDashboardFragment", "Showing: $titleForThisContainer (ID: $containerId)")
                    }
                } else {
                    Log.w("DefaultDashboardFragment", "No title mapping found for container ID: $containerId")
                }
            }
        }
    }

}



