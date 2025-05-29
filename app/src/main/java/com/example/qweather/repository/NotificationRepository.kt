package com.example.qweather.repository

import com.example.qweather.data.network.api_call.getNotificationsApi
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Dates
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository {

suspend fun getNotifications(
    page: Int,
    userId: String = "109740",
    lang: String = "en"
): List<Notification> = withContext(Dispatchers.IO) {
    val apiResponse = getNotificationsApi(page, userId, lang)

    val notificationList = mutableListOf<Notification>()

    for (group in apiResponse.response.result.list) {
        notificationList.add(Dates(year = group.year, month = group.month))
        for (item in group.data) {
            notificationList.add(
                NotificationModel(
                    title = item.title,
                    publicMessage = item.publicMessage,
                    date = item.created_at
                )
            )
        }
    }

    return@withContext notificationList
}}