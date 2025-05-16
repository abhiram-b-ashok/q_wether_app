package com.example.qweather.ui.side_nav_fragments.disclaimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDisclaimerBinding


class DisclaimerFragment : Fragment() {
    private lateinit var binding: FragmentDisclaimerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisclaimerBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}