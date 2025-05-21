package com.example.qweather.data.models.activities

import org.json.JSONObject

data class ActivityModel(
    val id: Int,
    val title: String,
    val image: String,
    val label: String,
    val content: String,

    )


data class ActivitiesApiResponse(
    val status: Boolean,
    val result: ActivitiesResult?
)

data class ActivitiesResult(
    val activities: ActivitiesData
)

data class ActivitiesData(
    val current_page: Int,
    val data: List<ActivityModel>
)

fun JSONObject.toActivitiesApiResponse(): ActivitiesApiResponse {
    val responseObject = this.getJSONObject("Response")
    return ActivitiesApiResponse(
        status = responseObject.getBoolean("status"),
        result = responseObject.optJSONObject("result")?.toActivityResult()
    )
}

fun JSONObject.toActivityResult(): ActivitiesResult {
    return ActivitiesResult(
        activities = this.getJSONObject("activities").toActivitiesData()
    )
}

fun JSONObject.toActivitiesData(): ActivitiesData {
    val dataArray = this.getJSONArray("data")
    val activityList = mutableListOf<ActivityModel>()
    for (i in 0 until dataArray.length()) {
        val obj = dataArray.getJSONObject(i)
        activityList.add(
            ActivityModel(
                id = obj.getInt("id"),
                title = obj.getString("title"),
                image = obj.getString("image"),
                label = obj.getString("label"),
                content = obj.getString("content"),

            )
        )
    }
    return ActivitiesData(
        current_page = this.getInt("current_page"),
        data = activityList
    )
}


