package com.example.qweather.utility_funtions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun temperatureConverter(temperature: Double, unit: String): Double {
    return when (unit) {
        "°C" -> temperature
        "F" -> (temperature * 9 / 5) + 32
        "K" -> temperature + 273.15
        else -> throw IllegalArgumentException("Invalid unit: $unit")
}}

fun windConverter(speed: Double, unit: String): Double {
    return when (unit) {
        "m/s" -> speed
        "km/h" -> speed * 3.6
        "mph" -> speed * 2.23694
        "kt" -> speed * 0.514444
        else -> throw IllegalArgumentException("Invalid unit: $unit")
}}

fun tideConverter(height: Double, unit: String): Double {
    return when (unit) {
        "m" -> height
        "ft" -> height * 3.28084
        else -> throw IllegalArgumentException("Invalid unit: $unit")
}}


//setting up compass points
val compassPoints = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
val compassAngles = listOf(0, 45, 90, 135, 180, 225, 270, 315)

 fun getCompassIndex(windDirection: Int): Int {
    var closestIndex = 0
    var minDiff = 360.0

    for (i in compassAngles.indices) {
        val diff = Math.abs(windDirection - compassAngles[i]).toDouble()
        if (diff < minDiff) {
            minDiff = diff
            closestIndex = i
        }
    }
    return closestIndex
}



