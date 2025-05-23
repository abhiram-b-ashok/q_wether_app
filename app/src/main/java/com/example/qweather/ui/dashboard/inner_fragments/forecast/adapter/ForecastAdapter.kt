package com.example.qweather.ui.dashboard.inner_fragments.forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.forecast.ForecastModel
import com.example.qweather.databinding.CellForecastDailyItemsBinding
import java.text.SimpleDateFormat

class ForecastAdapter(private val list: List<ForecastModel>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = CellForecastDailyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return 7
    }

    class ForecastViewHolder(private val binding: CellForecastDailyItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastModel) {


            binding.day.text =item.date
            binding.temperature.text = item.temperature.toString()
            binding.temperatureUnit.text = item.temperature_unit
            binding.forecastIcon.setImageResource(item.icon)
            binding.condition.text = item.temperature_min.toString()
        }


    }

}