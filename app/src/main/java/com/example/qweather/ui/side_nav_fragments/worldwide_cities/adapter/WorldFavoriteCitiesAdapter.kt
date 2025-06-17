package com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.forecast.SavedForecastModel
import com.example.qweather.databinding.CellWorldCitiesItemBinding

class WorldFavoriteCitiesAdapter(private val list: List<SavedForecastModel>) :
    RecyclerView.Adapter<WorldFavoriteCitiesAdapter.WorldFavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldFavoriteViewHolder {
        val binding =
            CellWorldCitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorldFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorldFavoriteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class WorldFavoriteViewHolder(private val binding: CellWorldCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(city: SavedForecastModel) {
                binding.apply{
                    cityName.text = city.cityName
                    cityCurrentTime.text = city.date
                    cityTemp.text = city.temperature.toString()
                    cityWeatherDesc.text = city.weatherType
                }
            }
    }
}