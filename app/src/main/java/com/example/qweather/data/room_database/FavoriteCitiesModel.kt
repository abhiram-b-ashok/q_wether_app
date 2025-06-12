package com.example.qweather.data.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_cities")
data class FavoriteCitiesModel(
    @PrimaryKey val cityId: Int = 0,
    val cityName: String,
    val longitude: Double,
    val latitude: Double,
    var isSaved: Boolean = false
)
//check this and i want temperature, time, weather type also in recycler view of the selected city. i only got city name, city id, latitude and longitude.