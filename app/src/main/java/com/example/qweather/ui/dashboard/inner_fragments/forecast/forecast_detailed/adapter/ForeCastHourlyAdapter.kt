package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.cities_weather.HourlyForecast
import com.example.qweather.databinding.CellForecastHourlyItemsBinding
import com.example.qweather.utility_funtions.temperatureConverter


class ForeCastHourlyAdapter(private val list: List<HourlyForecast>) : RecyclerView.Adapter<ForeCastHourlyAdapter.ForecastHourlyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHourlyViewHolder {
        val binding = CellForecastHourlyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastHourlyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ForecastHourlyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return 6
    }

    class ForecastHourlyViewHolder(private val binding: CellForecastHourlyItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyForecast) {
            val sharedPreferences = binding.root.context.getSharedPreferences("settingPreference", Context.MODE_PRIVATE)
            var tempUnit = sharedPreferences.getString("selectedTemperature", "°C")

            binding.apply {
                time.text = item.time
                temperature.text = "${temperatureConverter(item.temperature, tempUnit.toString())}$tempUnit"
                windSpeed.text = "${item.wind_power} ${item.wind_power_unit}"
                windDirection.text = item.wind_direction_text

            }

        }

    }

}