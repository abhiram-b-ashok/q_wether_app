package com.example.qweather.ui.dashboard.inner_fragments.tides.tide_detailed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.qweather.R
import com.example.qweather.databinding.FragmentTideDetailedBinding
import com.example.qweather.repository.TideAPI
import com.example.qweather.repository.WeatherRepository
import com.example.qweather.ui.dashboard.inner_fragments.tides.Status
import com.example.qweather.ui.dashboard.inner_fragments.tides.TidalViewData
import com.example.qweather.ui.dashboard.inner_fragments.tides.TideXmlDocument
import com.example.qweather.utility_funtions.temperatureConverter
import com.example.qweather.view_models.city_weather.WeatherViewModel
import com.example.qweather.view_models.city_weather.WeatherViewModelFactory
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class TideDetailedFragment : Fragment() {
    private lateinit var binding: FragmentTideDetailedBinding
    var dateDown = 0
    var dailyForecastSize = 0
    private lateinit var sharedPrefs: SharedPreferences
    private var areaId: Int = 1
    private lateinit var weatherViewModel: WeatherViewModel
    private var tidalData: TidalViewData? = null
    private val tideUnit: String by lazy {
        try {
            requireContext()
                .getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
                .getString("selectedTide", "m") ?: "m"
        } catch (e: ClassCastException) {
            "m"
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefs = context.getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
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
        getTides()
        val sharedPreferences =
            requireContext().getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
        var tempUnit = sharedPreferences.getString("selectedTemperature", "°C")

        val weatherRepository = WeatherRepository()
        val viewModelFactory = WeatherViewModelFactory(weatherRepository)
        weatherViewModel =
            ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        val lat = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LATITUDE", 0L))
        val lon = Double.fromBits(sharedPrefs.getLong("LAST_CITY_LONGITUDE", 0L))
        val isQatar = sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)
        weatherViewModel.loadWeather(lat, lon, isQatar)

        binding.apply {

            cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")

            prevBt.setOnClickListener {

                if (dateDown > 0) {
                    dateDown--
                    weatherViewModel.loadWeather(lat, lon, isQatar)
                    areaId--
                    getTides()
                }
                else{
                    prevBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $dailyForecastSize is out of bounds")
                }

            }
            nextBt.setOnClickListener {
                if (dateDown < dailyForecastSize-1) {
                    dateDown++
                    weatherViewModel.loadWeather(lat, lon, isQatar)
                    areaId++
                    getTides()
                }
                else{
                    nextBt.isEnabled = false
                    Log.d("ForecastDetailedFragment", "Daily Forecast: $dailyForecastSize is out of bounds")
                }

            }

            weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
                result?.dailyForecast?.let { forecast ->
                    dailyForecastSize = (forecast.size)
                    Log.d("ForecastDetailedFragment", "Daily Forecastsize: ${forecast.size}")
                    temp.text =temperatureConverter(forecast[dateDown].temperature,
                        tempUnit.toString()).toString()
                    tempeUnit.text = tempUnit
                    condition.text = forecast[dateDown].weather_type

                    if (forecast[dateDown].weather_type == "Clear"){
                        cloudIcon.setImageResource(R.drawable.sun)
                    }
                    else if (forecast[dateDown].weather_type == "Overcast Clouds"){
                        cloudIcon.setImageResource(R.drawable.few_clouds_ic)
                    }
                    else if (forecast[dateDown].weather_type == "Rain"){
                        cloudIcon.setImageResource(R.drawable.rain_ic)
                    }
                    else if (forecast[dateDown].weather_type == "Snowy"){
                        cloudIcon.setImageResource(R.drawable.snow_ic)
                    }
                    else if (forecast[dateDown].weather_type == "Dusty"){
                        cloudIcon.setImageResource(R.drawable.dust_ic)
                    }
                    else if (forecast[dateDown].weather_type == "Mist"){
                        cloudIcon.setImageResource(R.drawable.mist_ic)
                    }
                    else{
                        cloudIcon.setImageResource(R.drawable.cloud_group)
                    }

                    val originalDateString = forecast[dateDown].date
                    val inputFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a",Locale.ENGLISH)
                    val outputFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.ENGLISH)

                    try {
                        val dateTime1 = LocalDateTime.parse(originalDateString, inputFormatter)
                        val formattedDate = dateTime1.format(outputFormatter)
                        date.text = formattedDate
                        binding.dateMain.text = formattedDate
                    } catch (e: Exception) {
                        Log.e("ForecastDetailedFragment", "Error formatting date: ${e.message}")
                        date.text = originalDateString
                    }
                }
            }
        }
    }

    private fun getTides() {
        lifecycleScope.launch {
            val xmlResponse: String? = TideAPI.getTidesData()

            if (xmlResponse != null) {
                try {
                    val parser = XmlPullParserFactory.newInstance().newPullParser()
                    parser.setInput(StringReader(xmlResponse))
                    val tidesDocument = TideXmlDocument.readXml(parser)

                    if (true) {
                        val newTidalData = TidalViewData()
                        newTidalData.status = Status.SUCCESS
                        newTidalData.document = tidesDocument
                        newTidalData.area = tidesDocument.areaTags?.firstOrNull { it.id == areaId.toString() }

                        if (newTidalData.area?.valueTags != null) {
                            val values = newTidalData.area!!.valueTags!!
                            newTidalData.currentHeightMeters = values.lastOrNull()?.value

                            val maxValueTag = values.maxByOrNull { it.value }
                            newTidalData.maxTideHeightMeters = maxValueTag?.value
                            newTidalData.maxTideHeightHour = maxValueTag?.hour

                            val minValueTag = values.minByOrNull { it.value }
                            newTidalData.minTideHeightMeters = minValueTag?.value
                            newTidalData.minTideHeightHour = minValueTag?.hour

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
//            binding.tideValue.text = tidalData!!.currentHeightMeters.toString()
            binding.tideValue.text = sharedPrefs.getString("LAST_TIDE_HEIGHT", "0.0").toString()
            binding.time.text = tidalData!!.lastUpdatedTime.toString()
            binding.tideHighValue.text = tidalData!!.maxTideHeightMeters.toString()
            binding.tideLowValue.text = tidalData!!.minTideHeightMeters.toString()
            binding.tideUnit.text = tideUnit.toString()
            binding.tideLowUnit.text = tideUnit.toString()
            binding.tideHighUnit.text = tideUnit.toString()

//            val values = tidalData?.area?.valueTags ?: return
            val values = tidalData?.area?.valueTags?.take(24) ?: return

            val tideHeights = values.map { valueTag ->
                valueTag.value
            }
            if (areaId != 1){
                binding.tideFlow.setTideData(tideHeights,0.0)
                return
            }
            val currentHeight = tidalData!!.currentHeightMeters
            binding.tideFlow.setTideData(tideHeights,currentHeight )
        } else {
            Log.e("TidesFragment", "Failed to fetch tides data.")
        }
    }
}
