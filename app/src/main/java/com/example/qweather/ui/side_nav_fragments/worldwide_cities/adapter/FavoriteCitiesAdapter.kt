package com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.databinding.CellWorldCitiesItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteCitiesAdapter(
    private var favorites: List<FavoriteCitiesModel>
) : RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: CellWorldCitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: FavoriteCitiesModel) {
            val sharedPreferences = binding.root.context.getSharedPreferences("CityPrefs", 0)
            binding.cityName.text = city.cityName
            binding.cityTemp.text = "0.0 ${sharedPreferences.getString("tempUnit", "C")}"
            binding.cityWeatherDesc.text = "_____"
            binding.cityCurrentTime.text = "__:__"
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
        Log.i("@@@@@@FavoriteCitiesModel", "updateList: $favorites")
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): FavoriteCitiesModel? {
        return if (position >= 0 && position < favorites.size) {
            favorites[position]
        } else {
            null
        }
    }
    private fun convertTimestampToTime(timestamp: Long, pattern: String =  "HH:mm a"): String {
        val date = Date(timestamp *1000L)
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        return format.format(date)
    }

}



