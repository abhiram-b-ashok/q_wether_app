package com.example.qweather.view_models.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.qweather.repository.NotificationRepository
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationModel

class NotificationViewModel(notificationRepository: NotificationRepository) {
    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList: LiveData<List<Notification>> = _notificationList

    fun getNotifications() {


    }




}