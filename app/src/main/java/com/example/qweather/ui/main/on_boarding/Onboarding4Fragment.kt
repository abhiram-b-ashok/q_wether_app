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
import com.example.qweather.databinding.FragmentOnboarding4Binding


class Onboarding4Fragment : Fragment() {
    private lateinit var binding: FragmentOnboarding4Binding
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboarding4Binding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = requireContext().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding4Fragment_to_dashBoardFragment)
            sharedPreference.edit().putBoolean("isFirstTime", false).apply()

        }

    }


}