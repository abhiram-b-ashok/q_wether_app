package com.example.qweather.ui.dashboard.inner_fragments.moon_phase

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.R
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentMoonPhaseBinding
import com.example.qweather.repository.MoonPhaseRepository
import com.example.qweather.view_models.moon_phase.MoonPhaseViewModel
import com.example.qweather.view_models.moon_phase.MoonPhaseViewModelFactory

class MoonPhaseFragment : Fragment() {

    private lateinit var viewModel: MoonPhaseViewModel
    private lateinit var binding: FragmentMoonPhaseBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoonPhaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MoonPhaseRepository()
        viewModel = ViewModelProvider(this, MoonPhaseViewModelFactory(repository))[MoonPhaseViewModel::class.java]


        observeViewModel()

        val cityId = arguments?.getInt("CITY_ID") ?: 0
        viewModel.loadMoonPhase(cityId)

    }

    private fun observeViewModel() {
        viewModel.moonPhase.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                   binding.apply {
                       moonSetTime.text = result.data?.moonData[0]?.moonset.toString()
                       moonRiseTime.text = result.data?.moonData[0]?.moonrise.toString()
                       fullMoonDate.text = result.data?.moonData[0]?.date.toString()
                       newMoonDate.text = result.data?.moonData[0]?.date.toString()


                   }
                }
                is NetworkResult.Error -> {
                    Log.e("error","error")
                }
            }
        }
    }
}
