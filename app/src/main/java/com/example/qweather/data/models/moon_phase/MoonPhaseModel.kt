package com.example.qweather.data.models.moon_phase

data class MoonPhaseModel(
    val moonrise: String,
    val moonset: String,
    val phase_name: String,
    val current_fullmoon: String,
    val current_newmoon: String,
    val date: String,
)
