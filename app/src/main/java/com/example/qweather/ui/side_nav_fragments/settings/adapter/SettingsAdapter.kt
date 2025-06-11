package com.example.qweather.ui.side_nav_fragments.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.R
import com.example.qweather.data.models.settings.DashboardSettingsModel
import com.example.qweather.databinding.CellSettingsItemsBinding

class SettingsAdapter(private val items: List<DashboardSettingsModel>) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {
    var onItemSelect: ((DashboardSettingsModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val binding =
            CellSettingsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(items[position],position, onItemSelect)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SettingsViewHolder(private val binding: CellSettingsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: DashboardSettingsModel,
            position: Int,
            onItemSelect: ((DashboardSettingsModel) -> Unit)?
        ) {
            binding.apply {
                toggleImg.setImageResource(item.toggleImage)
                label.text = item.title
                toggleImg.setOnClickListener {
                    onItemSelect?.invoke(item)
                }
//                if (item.isSelect) {
//                    toggleImg.setImageResource(R.drawable.selected_ic)
//                } else if (item.title == "Current Weather") {
//                    toggleImg.setImageResource(R.drawable.default_selected_ic)
//                } else {
//                    toggleImg.setImageResource(R.drawable.to_select_ic)
//                }
                if (item.title == "Current Weather") {
                    toggleImg.setImageResource(R.drawable.default_selected_ic)
                    toggleImg.isEnabled = false
                } else {
                    toggleImg.isEnabled = true
                    toggleImg.setImageResource(if (item.isSelect) R.drawable.selected_ic else R.drawable.to_select_ic)
                }

            }

        }


    }
}
