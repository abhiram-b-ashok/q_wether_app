package com.example.qweather.data.models.moon_phase

import com.google.gson.annotations.SerializedName


data class MoonPhaseResponseWrapper(
    @SerializedName("Response")
    val response: MoonPhaseResponse
)

data class MoonPhaseResponse(
    val status: Boolean,
    val result: MoonPhaseModel
)

data class MoonPhaseModel(
    @SerializedName("moon_phases") val moonPhases: MoonPhases,
    @SerializedName("moon_data") val moonData: List<MoonDayData>
)

data class MoonPhases(
    @SerializedName("current_new_moon") val currentNewMoon: String,
    @SerializedName("current_full_moon") val currentFullMoon: String,
    @SerializedName("next_new_moon") val nextNewMoon: String,
    @SerializedName("next_full_moon") val nextFullMoon: String
)

data class MoonDayData(
    val dt: Long,
    val moonrise: Long,
    val moonset: Long,
    @SerializedName("moon_phase") val moonPhase: Double,
    @SerializedName("phase_name") val phaseName: String,
    val date: String
)
