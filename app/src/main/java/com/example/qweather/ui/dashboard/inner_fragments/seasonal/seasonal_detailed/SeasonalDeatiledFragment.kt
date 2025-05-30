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
        binding = FragmentSeasonalDeatiledBinding.inflate(layoutInflater, container, false)
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

            if (args.sign == "Aries") {
                starIcon.setImageResource(R.drawable.ic_aries)
            } else if (args.sign == "Taurus") {
                starIcon.setImageResource(R.drawable.ic_taurus)
            } else if (args.sign == "Gemini") {
                starIcon.setImageResource(R.drawable.ic_gemini)
            } else if (args.sign == "Cancer") {
                starIcon.setImageResource(R.drawable.ic_cancer)
            } else if (args.sign == "Leo") {
                starIcon.setImageResource(R.drawable.ic_leo)
            } else if (args.sign == "Virgo") {
                starIcon.setImageResource(R.drawable.ic_virgo)
            } else if (args.sign == "Libra") {
                starIcon.setImageResource(R.drawable.ic_libra)
            } else if (args.sign == "Scorpio") {
                starIcon.setImageResource(R.drawable.ic_scorpio)
            } else if (args.sign == "Sagittarius") {
                starIcon.setImageResource(R.drawable.ic_sagittarius)
            } else if (args.sign == "Capricorn") {
                starIcon.setImageResource(R.drawable.ic_capricorn)
            } else if (args.sign == "Aquarius") {
                starIcon.setImageResource(R.drawable.ic_aquarius)
            } else if (args.sign == "Pisces") {
                starIcon.setImageResource(R.drawable.ic_pisces)
            }

        }

    }


}