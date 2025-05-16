package com.example.qweather.ui.side_nav_fragments.notifications.adapter

const val ITEM = 1
const val DATE = 2

interface Notification {
fun provideType(): Int

}

data class NotificationModel(
    val title: String,
    val description : String,
    val date : String,
): Notification {
    override fun provideType(): Int {
        return ITEM
    }
}

data class Date (
    val date : String
):
        Notification {
    override fun provideType(): Int {
        return DATE
    }
}
