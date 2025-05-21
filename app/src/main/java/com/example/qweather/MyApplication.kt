package com.example.qweather

import android.app.Application
import com.example.qweather.data.network.NetworkHandler

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkHandler.init()
    }
}
