package com.example.qweather.data.room_database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_cities")
data class FavoriteCitiesModel(
    @PrimaryKey val cityId: Int = 0,
    val cityName: String,
    var isSaved: Boolean = false
)


