package com.example.qweather.ui.side_nav_fragments.notifications_center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.databinding.FragmentNotificationsBinding
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Date
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationAdapter
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationModel


class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
//    private lateinit var notificationAdapter: NotificationAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = arrayListOf<Notification>(
            Date("April 2025"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
            Date("May 2025"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
            NotificationModel("Weather","Morning cloud, then increasing fine spells. Odd afternoon shower.","Monday, May 23, 2022, 12:30 am"),
        )

//        notificationAdapter= NotificationAdapter(list)
//        binding.notificationsRecycler.adapter = notificationAdapter


    }


}