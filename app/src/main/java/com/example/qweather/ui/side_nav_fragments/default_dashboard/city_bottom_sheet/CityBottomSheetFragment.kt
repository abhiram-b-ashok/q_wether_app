package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.core.graphics.toColorInt


class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val qatarCities = arrayListOf(
            QatarCitiesModel("Doha"),
            QatarCitiesModel("Al-Ahsa"),
            QatarCitiesModel("Al-Qusais"),
            QatarCitiesModel("Al-Ushuaia"),
        )
        qatarAdapter = QatarAdapter(qatarCities)


        val worldCities = arrayListOf(
            WorldCitiesModel("Dubai"),
            WorldCitiesModel("Abu Dhabi"),
            WorldCitiesModel("Sharjah"),
            WorldCitiesModel("Ajman"),
        )
        worldAdapter = WorldAdapter(worldCities)

        selectedType(1)

        binding.apply {
            qatarButton.setOnClickListener {
                selectedType(1)


            }
            worldwideButton.setOnClickListener {
                selectedType(2)


            }
            backButton.setOnClickListener {
                dismiss()
            }

        }

        qatarAdapter.onItemClickListener = { item ->
            sendResult(item.cityName)
            Log.d("CitySelection", "Selected city: ${item.cityName}")
            dismiss()

        }
        worldAdapter.onItemClickListener = { item ->
            sendResult(item.cityName)
            Log.d("CitySelection", "Selected city: ${item.cityName}")
            dismiss()
        }

    }

    fun selectedType(type: Int) {
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
            Log.d("CitySelection", "Sending city: $city")
            putString("selectedCity", city)
        })

    }

}