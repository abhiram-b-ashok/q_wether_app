package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.databinding.CellQatarCitiesBinding

class QatarAdapter(private var cities: MutableList<QatarCitiesModel>) : RecyclerView.Adapter<QatarAdapter.QatarViewHolder>() {
    var onItemClickListener: ((QatarCitiesModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QatarViewHolder {
        val binding = CellQatarCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QatarViewHolder, position: Int) {
        holder.bind(cities[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun updateList(newCities: List<QatarCitiesModel>) {
        cities.clear()
        cities.addAll(newCities)
        notifyDataSetChanged()  // refreshes the whole list
    }

    class QatarViewHolder(private val binding: CellQatarCitiesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(city: QatarCitiesModel, onItemClickListener: ((QatarCitiesModel) -> Unit)?) {
            binding.qatarCityName.text = city.cityName
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(city)
            }
        }
    }
}
