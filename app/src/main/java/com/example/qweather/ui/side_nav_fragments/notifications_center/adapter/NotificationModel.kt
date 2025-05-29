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

data class NotificationApiResponse(
    val response: ResponseData,

)

data class ResponseData(
    val status: Boolean,
    val result: Result
)

data class Result(
    val list: List<NotificationGroup>
)



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

fun JSONObject.toNotificationApiResponse(): NotificationApiResponse {
    val responseObj = this.getJSONObject("Response")
    val status = responseObj.getBoolean("status")
    val resultObj = responseObj.getJSONObject("result")
    val listJsonArray = resultObj.getJSONArray("list")

    val groups = mutableListOf<NotificationGroup>()

    for (i in 0 until listJsonArray.length()) {
        val groupObj = listJsonArray.getJSONObject(i)
        val year = groupObj.getString("year")
        val month = groupObj.getString("month")
        val dataArray = groupObj.getJSONArray("data")

        val notifications = mutableListOf<NotificationItem>()
        for (j in 0 until dataArray.length()) {
            val itemObj = dataArray.getJSONObject(j)
            val id = itemObj.getInt("id")
            val title = itemObj.getString("title")
            val publicMessage = itemObj.getString("publicMessage")
            val createdAt = itemObj.getString("created_at")

            notifications.add(
                NotificationItem(id, title, publicMessage, createdAt)
            )
        }

        groups.add(NotificationGroup(year, month, notifications))
    }

    return NotificationApiResponse(
        response = ResponseData(
            status = status,
            result = Result(groups)
        )
    )
}


