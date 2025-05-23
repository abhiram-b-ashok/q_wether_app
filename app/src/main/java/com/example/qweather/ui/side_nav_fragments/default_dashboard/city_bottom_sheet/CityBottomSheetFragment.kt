package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.view_models.cities.CityViewModel
import com.example.qweather.view_models.cities.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment




class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var viewModel: CityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCityBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelProviderFactory(CitiesRepository())
        viewModel = ViewModelProvider(this, factory)[CityViewModel::class.java]

        // Initialize adapters with empty lists (immutable lists are fine here)
        setupAdapters(emptyList(), emptyList())

        observeCities()

        binding.qatarButton.setOnClickListener { selectedType(1) }
        binding.worldwideButton.setOnClickListener { selectedType(2) }
        binding.backButton.setOnClickListener { dismiss() }

        // Show Qatar cities by default
        selectedType(1)

        // Fetch cities data from API
        viewModel.fetchCities()
    }

    private fun observeCities() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    // Optionally show a loading indicator here
                    // e.g. binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    // Hide loading indicator if any
                    // e.g. binding.progressBar.visibility = View.GONE

                    val qatarCities = result.data?.response?.result?.cities?.qatar
                    val worldCities = result.data?.response?.result?.cities?.world

                    val qatarList = qatarCities?.map { QatarCitiesModel(cityName = it.name) } ?: emptyList()
                    val worldList = worldCities?.map { WorldCitiesModel(cityName = it.name) } ?: emptyList()

                    qatarAdapter.updateList(qatarList)
                    worldAdapter.updateList(worldList)
                }
                is NetworkResult.Error -> {
                    // Hide loading indicator if any
                    // e.g. binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), result.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupAdapters(qatarCities: List<QatarCitiesModel>, worldCities: List<WorldCitiesModel>) {
        qatarAdapter = QatarAdapter(qatarCities.toMutableList())
        worldAdapter = WorldAdapter(worldCities.toMutableList())

        qatarAdapter.onItemClickListener = {
            sendResult(it.cityName)
            dismiss()
        }

        worldAdapter.onItemClickListener = {
            sendResult(it.cityName)
            dismiss()
        }

        // Set Qatar adapter by default
        binding.locationsRecyclerView.adapter = qatarAdapter
    }

    private fun selectedType(type: Int) {
        binding.apply {
            if (type == 1) {
                qatarLabel.setTextColor("#8B1738".toColorInt())
                worldLabel.setTextColor(Color.WHITE)
                qatarButtonLayout.setBackgroundColor(Color.WHITE)
                worldwideButtonLayout.setBackgroundColor("#8B1738".toColorInt())
                locationsRecyclerView.adapter = qatarAdapter
                locationType.text = "Qatar - Cities"
            } else {
                worldLabel.setTextColor("#8B1738".toColorInt())
                qatarLabel.setTextColor(Color.WHITE)
                worldwideButtonLayout.setBackgroundColor(Color.WHITE)
                qatarButtonLayout.setBackgroundColor("#8B1738".toColorInt())
                locationsRecyclerView.adapter = worldAdapter
                locationType.text = "World - Wide Cities"
            }
        }
    }

    private fun sendResult(city: String) {
        parentFragmentManager.setFragmentResult("citySelectionKey", Bundle().apply {
            putString("selectedCity", city)
        })
    }
}

