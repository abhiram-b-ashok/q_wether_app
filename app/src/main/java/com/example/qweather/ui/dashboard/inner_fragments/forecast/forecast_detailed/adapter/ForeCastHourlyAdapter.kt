package com.example.qweather.ui.dashboard.inner_fragments.forecast.forecast_detailed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.cities_weather.HourlyForecast
import com.example.qweather.databinding.CellForecastHourlyItemsBinding


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

            binding.apply {
                time.text = item.time
                temperature.text = "${item.temperature}${item.temperature_unit}"
                windSpeed.text = "${item.wind_power} ${item.wind_power_unit}"
                windDirection.text = item.wind_direction_text

            }

        }

    }

}