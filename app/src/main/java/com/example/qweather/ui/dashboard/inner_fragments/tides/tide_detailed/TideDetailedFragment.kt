package com.example.qweather.ui.dashboard.inner_fragments.tides.tide_detailed

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.qweather.R
import com.example.qweather.databinding.FragmentTideDetailedBinding
import com.example.qweather.repository.TideAPI
import com.example.qweather.ui.dashboard.inner_fragments.tides.Status
import com.example.qweather.ui.dashboard.inner_fragments.tides.TidalViewData
import com.example.qweather.ui.dashboard.inner_fragments.tides.TideXmlDocument
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TideDetailedFragment : Fragment() {
    private lateinit var binding: FragmentTideDetailedBinding
    private var dateDown = 0
    private var areaId: String = "1"

    private var tidalData: TidalViewData? = null
    private val tideUnit: String by lazy {
        requireContext()
            .getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
            .getString("selectedTide", "m") ?: "m"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentTideDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            prevBt.setOnClickListener {


            }
            nextBt.setOnClickListener {

            }
        }

    }






}