package com.example.qweather.ui.dashboard.inner_fragments.marine_forecast.marine_forecast_detailed.marine_bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.data.models.marine_forecast.MarineForecast
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.databinding.FragmentMarineBottomSheetBinding
import com.example.qweather.repository.MarineForecastRepository
import com.example.qweather.view_models.marine_forecast.MarineForecastViewModel
import com.example.qweather.view_models.marine_forecast.MarineForecastViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class MarineBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMarineBottomSheetBinding
    private lateinit var viewModel: MarineForecastViewModel
    private lateinit var marineForecastRepository: MarineForecastRepository
    private val marineForecastList = arrayListOf<MarineForecast>()
    var dateDown = 0
    var marineForecastSize = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarineBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        marineForecastRepository = MarineForecastRepository()
        val viewModelFactory = MarineForecastViewModelFactory(marineForecastRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MarineForecastViewModel::class.java]
        viewModel.getMarineForecast()
        init()

        binding.apply {

            prevBt.setOnClickListener {

                if (dateDown > 0) {
                    dateDown--
                    viewModel.getMarineForecast()
                }
                else{
                    prevBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $marineForecastSize is out of bounds")
                }


            }
            nextBt.setOnClickListener {
                if (dateDown < marineForecastSize-1) {
                    dateDown++

                    viewModel.getMarineForecast()
                }
                else{
                    nextBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $marineForecastSize is out of bounds")
                }

            }
        }


    }

    fun init() {
        viewModel.marineForecastList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    Log.d("MarineBottomSheetFragment", "Loading...")
                }

                is NetworkResult.Success -> {
                        marineForecastSize = result.data?.size ?: 0
                       marineForecastList.clear()
                       marineForecastList.addAll(result.data ?: emptyList())
                      binding.apply {
                          val dateTimeString = marineForecastList[dateDown].DataDateTime
                          val offsetDateTime = OffsetDateTime.parse(dateTimeString)
                          val formatter = DateTimeFormatter.ofPattern("E, dd MMM, HH:MM")
                          val formattedDateTime = offsetDateTime.format(formatter)
                         dateMain.text = formattedDateTime
                          warningValue.text = marineForecastList[dateDown].FWarningText
                          weatherValue.text = marineForecastList[dateDown].FTemperature.toString()
                          windSpeedValue.text = marineForecastList[dateDown].FWindSpeed.toString()
                          windDirectionValue.text = marineForecastList[dateDown].FWindDirectionText
                          seaStateValue.text = marineForecastList[dateDown].FSeaStateText
                          currentSpeedValue.text = marineForecastList[dateDown].FCurrentSpeed
                          visibiltyValue.text = marineForecastList[dateDown].FVisibility.toString()
                      }

                }

                is NetworkResult.Error -> {
                    Log.d("MarineBottomSheetFragment", "Error: ${result.message}")

                }


            }

        }


    }
}