package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.saved_cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.databinding.CellWorldCitesBinding

class FavoriteCitiesAdapter(private val list : List<FavoriteCitiesModel>): RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
       val binding = CellWorldCitesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

       holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }
    class FavoriteViewHolder(private val binding: CellWorldCitesBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(city: FavoriteCitiesModel) {
            binding.qatarCityName.text = city.cityName
        }

    }
}