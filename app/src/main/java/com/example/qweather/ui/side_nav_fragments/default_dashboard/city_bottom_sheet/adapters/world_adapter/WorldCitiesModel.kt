package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter

import com.example.qweather.data.models.cities.WorldCityData

data class WorldCitiesModel(
    val cityName: String,
    val originalData: WorldCityData? = null,
    val longitude: Double,
    val latitude: Double,
    val cityId: Int,
    var isSelected: Boolean = false
)
