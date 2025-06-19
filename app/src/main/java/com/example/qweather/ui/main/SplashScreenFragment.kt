package com.example.qweather.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
       binding = FragmentSplashScreenBinding.inflate(LayoutInflater.from(context),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            delay(2000)
            if (sharedPreferences.getBoolean("isFirstTime", true)) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_onBoarding1Fragment)
            }
            else {
                findNavController().navigate(R.id.action_splashScreenFragment_to_dashBoardFragment)
            }

        }

    }



}