package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.R
import com.example.qweather.data.models.cities_weather.DailyWeather
import com.example.qweather.databinding.CellForecastDailyItemsBinding
import com.example.qweather.utility_funtions.temperatureConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.toString

class ForecastDailyAdapter(private val list: List<DailyWeather>) : RecyclerView.Adapter<ForecastDailyAdapter.ForecastDailyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastDailyViewHolder {
        val binding = CellForecastDailyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastDailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastDailyViewHolder, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ForecastDailyViewHolder(private val binding: CellForecastDailyItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyWeather, position: Int) {
            val sharedPreferences = binding.root.context.getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
            var tempUnit = sharedPreferences.getString("selectedTemperature", "°C")
            val formattedDisplayDate = try {
                val dateStringFromApi = item.date
                Log.d("ForecastAdapter", "Attempting to parse date: '$dateStringFromApi'")
                val inputFormatter = DateTimeFormatter.ofPattern("E, MMM d, yyyy h:mm a", Locale.ENGLISH)
                val localDateTime = LocalDateTime.parse(dateStringFromApi, inputFormatter)
                val date = localDateTime.toLocalDate()
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val dayOfMonth = date.dayOfMonth
                val month = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val year = date.year

                "$dayOfWeek,$month $dayOfMonth, $year"

            } catch (e: Exception) {
                Log.e("ForecastAdapter", "Error parsing date: '${item.date}'", e)
                item.date
            }
            binding.apply {
                day.text =formattedDisplayDate
                temperature.text = temperatureConverter(item.temperature, tempUnit.toString()).toString()
                temperatureUnit.text = tempUnit.toString()
                condition.text = item.weather_type
                maxTemp.text = temperatureConverter(item.temperature_max, tempUnit.toString()).toString()
                minTemp.text ="/${temperatureConverter(item.temperature_min, tempUnit.toString())}"
                tempeUnit.text = tempUnit
                if (position%2!=0) {
                    layout.setBackgroundResource(R.color.lightYellow)
                }
            }
        }
    }
}