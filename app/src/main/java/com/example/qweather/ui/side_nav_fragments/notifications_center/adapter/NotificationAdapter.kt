package com.example.qweather.ui.side_nav_fragments.notifications_center.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.qweather.databinding.CellNotificationDateBinding
import com.example.qweather.databinding.CellNotificationItemBinding

class NotificationAdapter (private val list: ArrayList<Notification>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object{
        const val ITEM = 1
        const val DATE = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM -> NotificationViewHolder(CellNotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            DATE -> DateViewHolder(CellNotificationDateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is NotificationViewHolder -> holder.bind(list[position] as NotificationModel)
            is DateViewHolder -> holder.bind(list[position] as Date)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NotificationViewHolder(private val binding: CellNotificationItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationModel) {
            binding.notificationTitle.text = notification.title
            binding.notificationDate.text = notification.date
            binding.notificationDescription.text = notification.description

        }


    }
    inner class DateViewHolder(private val binding: CellNotificationDateBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(date: Date) {
            binding.date.text = date.date
        }

    }
}