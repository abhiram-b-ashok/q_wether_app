package com.example.qweather.data.models.seasonal

import com.google.gson.annotations.SerializedName



data class SeasonalModel(
    @SerializedName("star_name")
    val starName: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("description")
    val description: String,
    @SerializedName("sign")
    val sign: String?,

    @SerializedName("sign_ar")
    val signAr: String?,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("icon_small")
    val iconSmall: String?,

    @SerializedName("star_name_ar")
    val starNameAr: String?,

    @SerializedName("description_ar")
    val descriptionAr: String?
)

data class SeasonalResponseWrapper(
    @SerializedName("Response")
    val response: SeasonalResponse
)

data class SeasonalResponse(
    val status: Boolean,
    val result: SeasonalModel
)