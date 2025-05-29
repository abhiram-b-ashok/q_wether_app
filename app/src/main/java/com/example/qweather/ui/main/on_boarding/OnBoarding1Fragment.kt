package com.example.qweather.ui.main.on_boarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentOnBoarding1Binding


class OnBoarding1Fragment : Fragment() {
    private lateinit var binding: FragmentOnBoarding1Binding
    private lateinit var sharedPrefs: SharedPreferences


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoarding1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.nextButton.setOnClickListener {  findNavController().navigate(R.id.action_onBoarding1Fragment_to_onBoarding2Fragment)}


            binding.apply {
                cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")
                temp.text = "${Double.fromBits(sharedPrefs.getLong("LAST_TEMPERATURE", 0L))}°C"
                condition.text = sharedPrefs.getString("LAST_WEATHER_TYPE", "Clear")
                approx.text = "Feels like ${Double.fromBits(sharedPrefs.getLong("LAST_FEELS_LIKE", 0L))}°C"
                precipPercent.text = Double.fromBits(sharedPrefs.getLong("LAST_HUMIDITY", 0L)).toString()
                windSpeed.text = Double.fromBits(sharedPrefs.getLong("LAST_WIND_SPEED", 0L)).toString()
                date.text = sharedPrefs.getString("LAST_DATE", "0.0")

            }
    }


}



