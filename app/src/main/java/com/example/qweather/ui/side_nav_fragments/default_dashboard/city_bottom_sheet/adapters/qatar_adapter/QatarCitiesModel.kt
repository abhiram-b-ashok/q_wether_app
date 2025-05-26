package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter

import com.example.qweather.data.models.cities.QatarCityData

data class QatarCitiesModel(
    val cityName: String,
    val originalData: QatarCityData? = null,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    var isSelectedItem: Boolean = false
)
