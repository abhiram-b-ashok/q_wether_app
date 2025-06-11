package com.example.qweather.ui.dashboard.inner_fragments.current_weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentCurrentWeatherBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragmentDirections
import com.example.qweather.utility_funtions.compassPoints
import com.example.qweather.utility_funtions.getCompassIndex
import com.example.qweather.utility_funtions.temperatureConverter
import com.example.qweather.utility_funtions.windConverter
import com.example.qweather.view_models.city_weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentWeatherFragment : Fragment() {
    private lateinit var binding: FragmentCurrentWeatherBinding
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
        var tempUnit = sharedPreferences.getString("selectedTemperature", "°C")
        var windUnit = sharedPreferences.getString("selectedWind", "km/h")


        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.dailyForecast?.let { current ->
                Log.d("WeatherVM", "Current Weather: ${current[0]}")


                binding.apply {
                    layout.setOnClickListener {
                        findNavController().navigate(DefaultDashboardFragmentDirections.actionDefaultDashboardFragmentToForecastDetailedFragment())
                    }
                    cityName.text = sharedPrefs.getString("LAST_SELECTED_CITY", "Qatar")
                    val timeStamp = current[0].date
                    Log.d("raw_timestamp","$timeStamp")
                    cityTime.text = timeStamp
                    cityTemperature.text = temperatureConverter(
                        current[0].temperature,
                        tempUnit.toString()
                    ).toString()
                    cityWeatherCondition.text = current[0].weather_type
                    feelsLikTemp.text = temperatureConverter(
                        current[0].feels_like_day,
                        tempUnit.toString()
                    ).toString()
                    tempeUnit.text = tempUnit
                    humiPercent.text = current[0].humidity.toString()
                    val windDirectionIndex = getCompassIndex(current[0].wind_direction)
                    windDirection.text = compassPoints[windDirectionIndex]
                    windSpeed.text = windConverter(current[0].wind_speed, windUnit.toString()).toString()
                    windSpeedUnit.text = windUnit
                    temperatureUnit.text = tempUnit
                    tempUp.text = temperatureConverter(
                        current[0].temperature_max,
                        tempUnit.toString()
                    ).toString()
                    tempDown.text = temperatureConverter(
                        current[0].temperature_min,
                        tempUnit.toString()
                    ).toString()
                    with(sharedPrefs.edit()) {
                        putLong(
                            "LAST_TEMPERATURE",
                            java.lang.Double.doubleToRawLongBits(current[0].temperature.toDouble())
                        )
                        putLong(
                            "LAST_TEMP_MIN",
                            java.lang.Double.doubleToRawLongBits(current[0].temperature_min.toDouble())
                        )
                        putLong(
                            "LAST_TEMP_MAX",
                            java.lang.Double.doubleToRawLongBits(current[0].temperature_max.toDouble())
                        )
                        putString("LAST_WEATHER_TYPE", current[0].weather_type)
                        putLong(
                            "LAST_FEELS_LIKE",
                            java.lang.Double.doubleToRawLongBits(current[0].feels_like_day.toDouble())
                        )
                        putLong(
                            "LAST_HUMIDITY",
                            java.lang.Double.doubleToRawLongBits(current[0].humidity.toDouble())
                        )
                        putLong(
                            "LAST_WIND_SPEED",
                            java.lang.Double.doubleToRawLongBits(current[0].wind_speed.toDouble())
                        )
                        putString("LAST_DATE", timeStamp)
                        putString("LAST_WIND_DIRECTION", compassPoints[windDirectionIndex])

                        apply()
                    }

                    if (current[0].weather_type == "Clear") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_clear_bg)
                        cityWeatherIcon.setImageResource(R.drawable.clear_sky_ic)
                    } else if (current[0].weather_type == "Dust") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_dust_bg)
                        cityWeatherIcon.setImageResource(R.drawable.dust_ic)
                    } else if (current[0].weather_type == "Rain") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_rain_bg)
                        cityWeatherIcon.setImageResource(R.drawable.rain_ic)
                    } else if (current[0].weather_type == "Stormy") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_thuder_storm_bg)
                        cityWeatherIcon.setImageResource(R.drawable.thunder_ic)
                    } else if (current[0].weather_type == "Snow") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_snow_bg)
                        cityWeatherIcon.setImageResource(R.drawable.snow_ic)
                    } else if (current[0].weather_type == "Mist") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_mist_bg)
                        cityWeatherIcon.setImageResource(R.drawable.mist_ic)
                    } else if (current[0].weather_type == "Few Clouds") {
                        currentWeatherLayout.setBackgroundResource(R.drawable.currrent_few_clouds_bg)
                        cityWeatherIcon.setImageResource(R.drawable.few_clouds_ic)
                    } else {
                        currentWeatherLayout.setBackgroundResource(R.drawable.current_shower_rain_bg)
                        cityWeatherIcon.setImageResource(R.drawable.shower_rain_ic)
                    }

                }
            }

        }

    }


}
