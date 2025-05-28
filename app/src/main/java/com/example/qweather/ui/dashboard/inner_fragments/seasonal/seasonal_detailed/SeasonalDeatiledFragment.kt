package com.example.qweather.ui.dashboard.inner_fragments.seasonal.seasonal_detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.qweather.R
import com.example.qweather.databinding.FragmentSeasonalDeatiledBinding


class SeasonalDeatiledFragment : Fragment() {
    private lateinit var binding: FragmentSeasonalDeatiledBinding
    private val args: SeasonalDeatiledFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeasonalDeatiledBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dateValue.text = args.date
            daysValue.text = args.days
            signValue.text = args.sign
            descriptionValue.text = args.description
            seasonName.text = args.season
        }

    }


}