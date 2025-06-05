package com.example.qweather.ui.dashboard.inner_fragments.tides

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.qweather.databinding.FragmentTidesBinding
import com.example.qweather.repository.TideAPI
import com.example.qweather.utility_funtions.tideConverter
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.Date

class TidesFragment : Fragment() {

    private lateinit var binding: FragmentTidesBinding

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
    ): View {
        binding = FragmentTidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTides()
    }

    private fun getTides() {
        lifecycleScope.launch {
            val xmlResponse: String? = TideAPI.getTidesData()

            if (xmlResponse != null) {
                try {
                    val parser = XmlPullParserFactory.newInstance().newPullParser()
                    parser.setInput(StringReader(xmlResponse))
                    val tidesDocument = TideXmlDocument.readXml(parser)

                    if (tidesDocument != null) {
                        // FIX: Instantiate with an empty constructor and set properties individually.
                        val newTidalData = TidalViewData()
                        newTidalData.status = Status.SUCCESS
                        newTidalData.document = tidesDocument
                        newTidalData.area = tidesDocument.areaTags?.firstOrNull { it.id == areaId }

                        if (newTidalData.area?.valueTags != null) {
                            val values = newTidalData.area!!.valueTags!!

                            val maxValueTag = values.maxByOrNull { it.value }
                            newTidalData.maxTideHeightMeters = maxValueTag?.value
                            newTidalData.maxTideHeightHour = maxValueTag?.hour

                            val minValueTag = values.minByOrNull { it.value }
                            newTidalData.minTideHeightMeters = minValueTag?.value
                            newTidalData.minTideHeightHour = minValueTag?.hour

                           newTidalData.currentHeightMeters = values.lastOrNull()?.value
                            newTidalData.lastUpdatedTime = Date()
                        }
                        tidalData = newTidalData


                        setData()
                        Log.d("TidesFragment", "tidalData: $tidalData")
                        Log.d("TidesFragment", "Successfully fetched and parsed tides.")
                    } else {
                        Log.e("TidesFragment", "Failed to parse XML content.")
                        val newTidalData = TidalViewData()
                        newTidalData.status = Status.ERROR
                        tidalData = newTidalData
                    }

                } catch (e: Exception) {
                    Log.e("TidesFragment", "Error parsing XML response", e)
                    val newTidalData = TidalViewData()
                    newTidalData.status = Status.ERROR
                    tidalData = newTidalData
                }
            } else {
                Log.e("TidesFragment", "API call failed. Response was null.")
                val newTidalData = TidalViewData()
                newTidalData.status = Status.ERROR
                tidalData = newTidalData
            }
        }
    }

    private fun setData() {

        if (tidalData?.status == Status.SUCCESS) {
            binding.tideValue.text = tidalData!!.currentHeightMeters.toString()
            binding.time.text = tidalData!!.lastUpdatedTime.toString()
            binding.tideHighValue.text = tidalData!!.maxTideHeightMeters.toString()
            binding.tideLowValue.text = tidalData!!.minTideHeightMeters.toString()
            binding.tideUnit.text = tideUnit.toString()
            binding.tideLowUnit.text = tideUnit.toString()
            binding.tideHighUnit.text = tideUnit.toString()

        } else {
            Log.e("TidesFragment", "Failed to fetch tides data.")

        }
    }
}