package com.example.qweather.ui.side_nav_fragments.worldwide_cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentWorldWideCitiesBinding


class WorldWideCitiesFragment : Fragment() {
    private lateinit var binding: FragmentWorldWideCitiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentWorldWideCitiesBinding.inflate(layoutInflater,container, false)
        return binding.root
    }


}