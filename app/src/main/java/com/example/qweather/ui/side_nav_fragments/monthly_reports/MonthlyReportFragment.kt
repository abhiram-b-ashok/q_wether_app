package com.example.qweather.ui.side_nav_fragments.monthly_reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentMonthlyReportBinding


class MonthlyReportFragment : Fragment() {
    private lateinit var binding: FragmentMonthlyReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyReportBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


}