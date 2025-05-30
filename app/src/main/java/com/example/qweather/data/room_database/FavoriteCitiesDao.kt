package com.example.qweather.data.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoriteCitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteCity(city: FavoriteCitiesModel)

    @Delete
    fun deleteFavoriteCity(city: FavoriteCitiesModel)

    @Query("SELECT * FROM favorite_cities")
    fun getAllFavoriteCities(): List<FavoriteCitiesModel>

    @Query("SELECT * FROM favorite_cities WHERE cityId = :cityId")
    fun getFavoriteCityById(cityId: Int): FavoriteCitiesModel?

}