package com.example.qweather.ui.side_nav_fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    var selectedLanguage = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            englishButton.setOnClickListener {
                englishLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                arabicLabel.setTextColor(resources.getColor(R.color.white))
                englishButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                arabicButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
            arabicButton.setOnClickListener {
                arabicLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                englishLabel.setTextColor(resources.getColor(R.color.white))
                arabicButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                englishButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
            celciusButton.setOnClickListener {
                celciusLabel.setTextColor(resources.getColor(R.color.maroon))
                fahrenheitLabel.setTextColor(resources.getColor(R.color.white))
                kelvinLabel.setTextColor(resources.getColor(R.color.white))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            }
            fahrenheitButton.setOnClickListener {
                fahrenheitLabel.setTextColor(resources.getColor(R.color.maroon))
                celciusLabel.setTextColor(resources.getColor(R.color.white))
                kelvinLabel.setTextColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            }
            kelvinButton.setOnClickListener {
                kelvinLabel.setTextColor(resources.getColor(R.color.maroon))
                fahrenheitLabel.setTextColor(resources.getColor(R.color.white))
                celciusLabel.setTextColor(resources.getColor(R.color.white))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))

        }

            kmButton.setOnClickListener {
                kmLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
            knotButton.setOnClickListener {
                knotLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
            msButton.setOnClickListener {
                msLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
            mphButton.setOnClickListener {
                mphLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }

            mButton.setOnClickListener {
                mLabel.setTextColor(resources.getColor(R.color.maroon))
                ftLabel.setTextColor(resources.getColor(R.color.white))
                mButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                ftButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            }
            ftButton.setOnClickListener {
                ftLabel.setTextColor(resources.getColor(R.color.maroon))
                mLabel.setTextColor(resources.getColor(R.color.white))
                ftButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                mButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            }
    }
    }
}