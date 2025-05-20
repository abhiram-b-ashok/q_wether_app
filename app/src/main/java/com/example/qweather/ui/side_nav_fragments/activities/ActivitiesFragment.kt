package com.example.qweather.ui.side_nav_fragments.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentActivitiesBinding
import com.example.qweather.ui.side_nav_fragments.activities.adapter.ActivityAdapter


class ActivitiesFragment : Fragment() {
    private lateinit var binding: FragmentActivitiesBinding
    private lateinit var activityAdapter: ActivityAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root



    }



}