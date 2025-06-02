package com.example.qweather.ui.dashboard.inner_fragments.forecast.adapter

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

class ForecastAdapter(private val list: List<DailyWeather>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = CellForecastDailyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return 7
    }

    class ForecastViewHolder(private val binding: CellForecastDailyItemsBinding) : RecyclerView.ViewHolder(binding.root) {
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
                val month = date.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

                "$dayOfWeek,$month $dayOfMonth, $year"

            } catch (e: Exception) {
                Log.e("ForecastAdapter", "Error parsing date: '${item.date}'", e)
                item.date
            }
            binding.day.text =formattedDisplayDate
            binding.temperature.text = item.temperature.toString()
            binding.temperatureUnit.text = item.temperature_unit
            binding.condition.text = item.weather_type
            binding.minMaxTemp.text = "${item.temperature_min}/${item.temperature_max}${item.temperature_unit}"
            if (position%2!=0) {
                binding.layout.setBackgroundResource(R.color.lightYellow)

            }

        }


    }

}