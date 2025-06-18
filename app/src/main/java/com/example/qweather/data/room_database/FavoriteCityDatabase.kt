package com.example.qweather.data.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [FavoriteCitiesModel::class], version = 5)
abstract class FavoriteCityDatabase : RoomDatabase() {
    abstract fun favoriteCitiesDao(): FavoriteCitiesDao


    companion object {
        @Volatile
        private var INSTANCE: FavoriteCityDatabase? = null
        fun getDatabase(context: Context): FavoriteCityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteCityDatabase::class.java,
                    "favorite_city_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}