package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter

import com.example.qweather.data.models.city_search.CitySearchModel

data class WorldCitiesModel(
    val cityName: String,
    val originalData: CitySearchModel,
    val longitude: Double,
    val latitude: Double,
    val cityId: Int,
    var isSelected: Boolean = false
)
