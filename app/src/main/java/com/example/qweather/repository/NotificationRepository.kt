package com.example.qweather.repository

import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.network.api_call.getNotificationsApi
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Dates
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationModel
import com.google.type.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository {

    suspend fun getNotifications(): List<Notification> = withContext(Dispatchers.IO) {
        val apiResponse = getNotificationsApi()
        val notificationList = mutableListOf<Notification>()

//        apiResponse.response.result.list.forEach { group ->
//            notificationList.add(Dates(group.year, group.month))
//
//            group.data.forEach { item ->
//                notificationList.add(
//                    NotificationModel(
//                        title = item.title,
//                        publicMessage = item.publicMessage,
//                        date = item.created_at
//                    )
//                )
//            }
//        }

        return@withContext notificationList
    }
}
