package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.R
import com.example.qweather.data.models.cities_weather.DailyWeather
import com.example.qweather.databinding.CellForecastDailyItemsBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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
            val formattedDisplayDate = try {
                val dateStringFromApi = item.date
                Log.d("ForecastAdapter", "Attempting to parse date: '$dateStringFromApi'")

                val inputFormatter = DateTimeFormatter.ofPattern("E, MMM d, yyyy h:mm a", Locale.ENGLISH)
                val localDateTime = LocalDateTime.parse(dateStringFromApi, inputFormatter)
                val date = localDateTime.toLocalDate()
                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val dayOfMonth = date.dayOfMonth
                val year = date.year

                "$dayOfWeek, $dayOfMonth, $year"

            } catch (e: Exception) {
                Log.e("ForecastAdapter", "Error parsing date: '${item.date}'", e)
                item.date
            }
            binding.apply {
                day.text =formattedDisplayDate
                temperature.text = item.temperature.toString()
                temperatureUnit.text = item.temperature_unit
                condition.text = item.weather_type
                minMaxTemp.text = "${item.temperature_min}/${item.temperature_max}${item.temperature_unit}"
                if (position%2!=0) {
                    layout.setBackgroundResource(R.color.lightYellow)
                }

            }

        }

    }

}