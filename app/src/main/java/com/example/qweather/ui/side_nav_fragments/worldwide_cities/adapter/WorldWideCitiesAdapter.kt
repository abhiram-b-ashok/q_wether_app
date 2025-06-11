package com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.databinding.CellWorldCitiesItemBinding
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel

class FavoriteCitiesAdapter(
    private var favorites: List<FavoriteCitiesModel>
) : RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteViewHolder>() {

    var onRemoveClickListener: ((FavoriteCitiesModel) -> Unit)? = null

    inner class FavoriteViewHolder(private val binding: CellWorldCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: FavoriteCitiesModel) {
            binding.cityName.text = city.cityName
            binding.cityTemp.text = city.temperature
            binding.cityWeatherDesc.text = city.weatherType
            binding.cityCurrentTime.text = city.date

            // Click to remove from favorites
            binding.cityName.setOnClickListener {
                onRemoveClickListener?.invoke(city)
            }
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

    fun updateList(newFavorites: List<FavoriteCitiesModel>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}
