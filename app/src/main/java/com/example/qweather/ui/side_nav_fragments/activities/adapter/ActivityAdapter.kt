package com.example.qweather.ui.side_nav_fragments.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.databinding.CellActivitiesItemBinding

class ActivityAdapter(private val list: ArrayList<ActivityModel>) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = CellActivitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ActivityViewHolder(private val binding: CellActivitiesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activityModel: ActivityModel) {
            binding.activityTitle.text = activityModel.title
            Glide.with(binding.root.context).load(activityModel.image).into(binding.activityImg)
        }

    }
}