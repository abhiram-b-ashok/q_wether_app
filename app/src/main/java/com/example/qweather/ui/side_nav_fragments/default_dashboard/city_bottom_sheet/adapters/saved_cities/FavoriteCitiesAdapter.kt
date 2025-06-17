package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.saved_cities

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.R
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.databinding.CellWorldCitesBinding

class FavoriteCitiesAdapter(private var list : List<FavoriteCitiesModel>): RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteViewHolder>() {
    var onItemClickListener: ((FavoriteCitiesModel) -> Unit)? = null
    var onDeleteClickListener: ((FavoriteCitiesModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
       val binding = CellWorldCitesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

       holder.bind(list[position],onItemClickListener,onDeleteClickListener)

    }

    override fun getItemCount(): Int {
        return list.size
    }
    class FavoriteViewHolder(private val binding: CellWorldCitesBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(city: FavoriteCitiesModel, onItemClickListener: ((FavoriteCitiesModel) -> Unit)? = null, onDeleteClickListener: ((FavoriteCitiesModel) -> Unit)? = null) {
            binding.qatarCityName.text = city.cityName
            binding.selectionIcon.setImageResource(R.drawable.ic_star_filled)
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(city)
            }
            binding.selectionIcon.setOnClickListener {
                onDeleteClickListener?.invoke(city)
            }


        }

    }
    fun updateList(newFavorites: List<FavoriteCitiesModel>) {
        list = newFavorites
        Log.i("@@@@@@FavoriteCitiesModel", "updateList: $list")
        notifyDataSetChanged()
    }
}