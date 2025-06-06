package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter

import com.example.qweather.data.models.cities.QatarCityData

data class QatarCitiesModel(
    val cityName: String,
    val originalData: QatarCityData? = null,
    val longitude: Double,
    val latitude: Double,
    val cityId: Int,
    var isSelectedItem: Boolean = false
)
