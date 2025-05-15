package com.example.qweather.ui.dashboard.city_bottom_sheet.adapters.world_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.databinding.CellWorldCitesBinding

class WorldAdapter(private val cities: List<WorldCitiesModel>) : RecyclerView.Adapter<WorldAdapter.WorldViewHolder>() {

    var onItemClickListener: ((WorldCitiesModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldViewHolder {
        val binding =
            CellWorldCitesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorldViewHolder, position: Int) {
        holder.bind(cities[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return cities.size
    }


    class WorldViewHolder(private val binding: CellWorldCitesBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(city: WorldCitiesModel, onItemClickListener: ((WorldCitiesModel) -> Unit)?) {
                binding.qatarCityName.text = city.cityName
                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(city)
                }

            }
    }
}