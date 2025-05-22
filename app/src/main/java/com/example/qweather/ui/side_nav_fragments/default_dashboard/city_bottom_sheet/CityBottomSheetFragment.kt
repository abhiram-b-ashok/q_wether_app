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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.view_models.cities.CitiesViewModel
import com.example.qweather.view_models.cities.ViewModelProviderFactory
import kotlinx.coroutines.launch


class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var viewModel: CitiesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCityBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelProviderFactory(CitiesRepository())
        viewModel = ViewModelProvider(this, factory)[CitiesViewModel::class.java]

        observeCities()

        binding.qatarButton.setOnClickListener { selectedType(1) }
        binding.worldwideButton.setOnClickListener { selectedType(2) }
        binding.backButton.setOnClickListener { dismiss() }

        viewModel.fetchCities()
    }

    private fun observeCities() {
        viewModel.citiesResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    result.data?.let { citiesApiResponse ->
                        val qatarList = citiesApiResponse.response.result.cities.qatar.map {
                            QatarCitiesModel(it.name)
                        }

                        val worldList = citiesApiResponse.response.result.cities.world.map {
                            WorldCitiesModel(it.name)
                        }

                        setupAdapters(qatarList, worldList)
                        selectedType(1)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupAdapters(qatarCities: List<QatarCitiesModel>, worldCities: List<WorldCitiesModel>) {
        qatarAdapter = QatarAdapter(qatarCities)
        worldAdapter = WorldAdapter(worldCities)

        qatarAdapter.onItemClickListener = {
            sendResult(it.cityName)
            dismiss()
        }

        worldAdapter.onItemClickListener = {
            sendResult(it.cityName)
            dismiss()
        }
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

