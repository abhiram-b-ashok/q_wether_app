package com.example.qweather.ui.dashboard.inner_fragments.tides

//import android.app.ProgressDialog
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import com.example.qweather.R
//import com.example.qweather.databinding.FragmentTidesBinding
//import com.example.qweather.repository.TideAPI
//import kotlinx.coroutines.launch
//import org.json.JSONException
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.IOException
//import java.io.StringReader


//class TidesFragment : Fragment() {
//    private lateinit var binding: FragmentTidesBinding
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentTidesBinding.inflate(layoutInflater,container,false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//     getTides()
//
//    }
//
//
//    private fun getTides() {
//        lifecycleScope.launch {
//
//            val response = TideAPI.getTidesData()
//
//            when (response.exception) {
//                null -> {
//                    val parser = XmlPullParserFactory.newInstance().newPullParser()
//                    parser.setInput(StringReader(response.rawResponse))
//                    val tides = TideXmlDocument.readXml(parser)
//                    if (tides!=null) {
//
//                        tidalData = TidalViewData()
//                        tidalData.status = Status.SUCCESS
//                        tidalData.document = tides
//                        // tidalData.area = getFirstArea(tides)
//                        tidalData.area = tides?.areaTags?.firstOrNull { it.id == areaId }
//                        setData()
//
//
//                    } else {
//
//                        tidalData = TidalViewData()
//                        tidalData.status = Status.ERROR
//
//                    }
//                }
//                is IOException -> {
//
//                    Log.d("TidesFragment", "calculateAdditionalTidalData: ${response.exception}")
//                }
//                is JSONException -> {
//
//
//                    Log.d("TidesFragment", "calculateAdditionalTidalData: ${response.exception}")
//                }
//                else -> {
//
//                   Log.d("TidesFragment", "calculateAdditionalTidalData: ${response.exception}")
//                }
//            }
//
//        }
//    }
//
//}
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.qweather.databinding.FragmentTidesBinding
import com.example.qweather.repository.TideAPI
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

// Assuming these classes and enums exist based on your original code
// enum class Status { SUCCESS, ERROR }
// class TidalViewData {
//     var status: Status? = null
//     var document: TideXmlDocument? = null
//     var area: TideXmlDocument.AreaTag? = null
// }
// object TideXmlDocument {
//     data class AreaTag(val id: String)
//     fun readXml(parser: XmlPullParser): TideXmlDocument? { /* ... parsing logic ... */ return null }
//     val areaTags: List<AreaTag> = emptyList()
// }


class TidesFragment : Fragment() {

    private lateinit var binding: FragmentTidesBinding

    // TODO: Replace "default_area" with the actual ID you need to find.
    private var areaId: String = "default_area"

    private var tidalData: TidalViewData? = null

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
                        tidalData = newTidalData
                        setData()
                        Log.d("TidesFragment", "tidalData: $tidalData") //tidalData: com.example.qweather.ui.dashboard.inner_fragments.tides.TidalViewData@3514d60
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
            binding.tideHighValue.text = tidalData!!.maxTideHeightMeters.toString()
        } else {
            binding.tideLowValue.text = "Error loading tides."

        }
    }
}