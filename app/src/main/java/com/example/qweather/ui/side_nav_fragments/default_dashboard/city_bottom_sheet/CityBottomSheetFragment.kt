package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qweather.R
import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.view_models.cities.CityViewModel
import com.example.qweather.view_models.cities.CityViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var viewModel: CityViewModel
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val repository = CitiesRepository(okHttpClient)
        viewModel = ViewModelProvider(
            this,
            CityViewModelFactory(repository)
        )[CityViewModel::class.java]

        setupAdapters()
        setupClickListeners()
        observeViewModel()

        viewModel.fetchCities()
        loadSavedCityType()
    }

    private fun setupAdapters() {
        qatarAdapter = QatarAdapter(mutableListOf()).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, true, city.longitude, city.latitude)
                dismiss()
            }
        }

        worldAdapter = WorldAdapter(mutableListOf()).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, false, city.longitude, city.latitude)
                dismiss()
            }
        }

        binding.locationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = qatarAdapter
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            qatarButton.setOnClickListener { selectedType(CityType.QATAR) }
            worldwideButton.setOnClickListener { selectedType(CityType.WORLD) }
            backButton.setOnClickListener { dismiss() }
        }
    }

    private fun observeViewModel() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    result.data?.let { response ->
                        updateAdapters(response)
                    }
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    showError(result.message ?: getString(R.string.error_unknown))
                }
            }
        }
    }

    private fun updateAdapters(response: CitiesResponse) {
        response.response.result.cities.let { cities ->
            qatarAdapter.updateList(
                cities.qatar.map { QatarCitiesModel(it.name, it) }
            )
            worldAdapter.updateList(
                cities.world.map { WorldCitiesModel(it.name, it) }
            )
        }
    }

    private fun selectedType(type: CityType) {
        binding.apply {
            val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            when (type) {
                CityType.QATAR -> {
                    qatarLabel.setTextColor(primaryColor)
                    worldLabel.setTextColor(Color.WHITE)
                    qatarButtonLayout.setBackgroundColor(Color.WHITE)
                    worldwideButtonLayout.setBackgroundColor(primaryColor)
                    locationsRecyclerView.adapter = qatarAdapter
                    locationType.text = getString(R.string.qatar_cities)
                }
                CityType.WORLD -> {
                    worldLabel.setTextColor(primaryColor)
                    qatarLabel.setTextColor(Color.WHITE)
                    worldwideButtonLayout.setBackgroundColor(Color.WHITE)
                    qatarButtonLayout.setBackgroundColor(primaryColor)
                    locationsRecyclerView.adapter = worldAdapter
                    locationType.text = getString(R.string.worldwide_cities)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            locationsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun sendSelection(cityName: String, isQatar: Boolean, latitude: Double, longitude: Double) {
        parentFragmentManager.setFragmentResult(
            "CITY_SELECTION_RESULT",
            bundleOf(
                "SELECTED_CITY" to cityName,
                "IS_QATAR" to isQatar,
                "LATITUDE" to latitude,
                "LONGITUDE" to longitude
            )
        )
        dismiss()
    }

    private fun loadSavedCityType(){
        if(!sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true))
        {
            selectedType(CityType.WORLD)
        }
        else
        {
            selectedType(CityType.QATAR)
        }
    }

}

enum class CityType {
    QATAR, WORLD
}




