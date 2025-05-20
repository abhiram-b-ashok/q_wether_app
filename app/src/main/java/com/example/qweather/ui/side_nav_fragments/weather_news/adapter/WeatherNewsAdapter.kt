package com.example.qweather.ui.side_nav_fragments.weather_news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qweather.data.models.weather_news.WeatherNewsModel
import com.example.qweather.databinding.CellWeatherNewsItemsBinding

class WeatherNewsAdapter(private val list: ArrayList<WeatherNewsModel>) : RecyclerView.Adapter<WeatherNewsAdapter.WeatherNewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherNewsViewHolder {
        val binding = CellWeatherNewsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherNewsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class WeatherNewsViewHolder(private val binding: CellWeatherNewsItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherNewsModel: WeatherNewsModel) {
            binding.newsTitle.text = weatherNewsModel.title
            binding.newsDate.text = weatherNewsModel.date
            Glide.with(binding.root.context).load(weatherNewsModel.image).into(binding.newsImg)
        }

    }
}