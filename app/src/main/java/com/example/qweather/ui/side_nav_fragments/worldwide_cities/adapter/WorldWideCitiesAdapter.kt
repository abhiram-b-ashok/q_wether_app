package com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.models.cities_weather.SavedForecastModel
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.databinding.CellWorldCitiesItemBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel

class FavoriteCitiesAdapter(
    private var favorites: List<SavedForecastModel>
) : RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: CellWorldCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: SavedForecastModel) {
            binding.cityName.text = city.cityName
            binding.cityTemp.text = city.temperature.toString()
            binding.cityWeatherDesc.text = city.weatherType
            binding.cityCurrentTime.text = city.date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = CellWorldCitiesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    fun updateList(newFavorites: List<SavedForecastModel>) {
        favorites = newFavorites
        Log.i("@@@@@@FavoriteCitiesModel", "updateList: $favorites")
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): SavedForecastModel? {
        return if (position >= 0 && position < favorites.size) {
            favorites[position]
        } else {
            null
        }
    }
}



