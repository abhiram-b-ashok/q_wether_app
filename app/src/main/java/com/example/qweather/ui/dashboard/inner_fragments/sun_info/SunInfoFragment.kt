package com.example.qweather.ui.dashboard.inner_fragments.sun_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.databinding.FragmentSunInfoBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.DefaultDashboardFragment
import com.example.qweather.view_models.city_weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SunInfoFragment : Fragment() {
    private lateinit var binding: FragmentSunInfoBinding
    private val weatherViewModel: WeatherViewModel by lazy {
        (parentFragment as? DefaultDashboardFragment)?.weatherViewModel
            ?: throw IllegalStateException("Must be in DefaultDashboardFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSunInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherViewModel.weatherResult.observe(viewLifecycleOwner) { result ->
            result?.dailyForecast?.let { currentWeather ->
                binding.apply {
                    val sunrise = currentWeather[0].sunrise
                    val sunset = currentWeather[0].sunset
                    val currentTime = System.currentTimeMillis() / 1000L

                    if (sunrise != null && sunset != null) {
                        sunriseTime.text = convertTimestampToTime(sunrise)
                        sunsetTime.text = convertTimestampToTime(sunset)

                        val totalDaylight = sunset - sunrise
                        val timeSinceSunrise = currentTime - sunrise

                        val sweepPercent = (timeSinceSunrise * 100f / totalDaylight).coerceIn(0f, 100f)
                        sunRiseView.setSweepAngleAnimator(sweepPercent)
                    }
                }
            }
        }
    }
    private fun convertTimestampToTime(timestamp: Long, pattern: String = "HH:mm a"): String {
        val date = Date(timestamp*1000L)
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        return format.format(date)
    }
}