package com.example.qweather.ui.dashboard.inner_fragments.marine_forecast.marine_forecast_detailed.marine_bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.marine_forecast.MarineForecast
import com.example.qweather.databinding.CellMarineForecastHourlyItemsBinding

class MarineForecastAdapter(private val marineForecastList: List<MarineForecast>) : RecyclerView.Adapter<MarineForecastAdapter.MarineViewHolder>() {
   var onItemClick: ((MarineForecast) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarineViewHolder {
        val binding=  CellMarineForecastHourlyItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarineViewHolder, position: Int) {
        holder.bind(marineForecastList[position],onItemClick )
    }

    override fun getItemCount(): Int {
        return marineForecastList.size
    }

    class MarineViewHolder(val binding: CellMarineForecastHourlyItemsBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(marineForecast: MarineForecast, onItemClick: ((MarineForecast) -> Unit)?) {
            binding.time.text = marineForecast.DataDateTime
            binding.root.setOnClickListener {
                onItemClick?.invoke(marineForecast)
            }

        }
    }
}