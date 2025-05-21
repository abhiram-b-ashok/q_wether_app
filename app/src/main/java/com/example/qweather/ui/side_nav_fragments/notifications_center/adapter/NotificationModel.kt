package com.example.qweather.ui.side_nav_fragments.notifications_center.adapter

import org.json.JSONObject

const val ITEM = 1
const val DATE = 2

interface Notification {
    fun provideType(): Int
}

data class NotificationModel(
    val title: String,
    val publicMessage: String,
    val date: String
): Notification {
    override fun provideType(): Int = ITEM
}

data class Dates(
    val year: String,
    val month: String
): Notification {
    override fun provideType(): Int = DATE
}




data class NotificationGroup(
    val year: String,
    val month: String,
    val data: List<NotificationItem>
)

data class NotificationItem(
    val id: Int,
    val title: String,
    val publicMessage: String,
    val created_at: String
)
