package com.example.qweather.ui.dashboard.inner_fragments.seasonal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels // Import this
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentSeasonalBinding
import com.example.qweather.repository.SeasonalRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragmentDirections
import com.example.qweather.view_models.seasonal.SeasonalViewModel
import com.example.qweather.view_models.seasonal.SeasonalViewModelFactory

class SeasonalFragment : Fragment() {
    private lateinit var binding: FragmentSeasonalBinding


    private val viewModel: SeasonalViewModel by viewModels {
        val seasonalRepository = SeasonalRepository() 
        SeasonalViewModelFactory(seasonalRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeasonalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadSeasonalData()



        viewModel.seasonalData.observe(viewLifecycleOwner) { seasonalData ->
            seasonalData?.let { season -> 
                Log.d("SeasonalFragment", "Received seasonal data: $season")
                binding.apply {
                    seasonValue.text = season.starName
                    dateValue.text = season.startDate
                    daysValue.text = season.duration.toString()
                    signValue.text = season.sign
                    viewDetailsButton.setOnClickListener {
                        findNavController().navigate(DefaultDashboardFragmentDirections.actionDefaultDashboardFragmentToSeasonalDeatiledFragment(season.starName, season.startDate, season.duration.toString(),
                            season.sign.toString(), season.description))
                    }
                }
            } ?: run {
                Log.d("SeasonalFragment", "Seasonal data is null")
            }
        }
    }
}