package com.example.qweather.data.models.tide
//
//// 1. Define your data models based on the XML structure
//data class TideModel(
//    val source: String,
//    val productionCenter: String,
//    val areas: List<Area>
//)
//
//data class Area(
//    val id: String,
//    val name: String,
//    val latitude: Double,
//    val longitude: Double,
//    val nameAr: String,
//    val values: List<TideValue>
//)
//
//data class TideValue(
//    val date: String,
//    val value1: Int,
//    val value: Double
//)
//
//// 2. NetworkResult sealed class (common pattern)
//sealed class NetworkResult<T> {
//    class Loading<T> : NetworkResult<T>()
//    data class Success<T>(val data: T) : NetworkResult<T>()
//    data class Error<T>(val message: String, val data: T? = null) : NetworkResult<T>()
//}
//
//// 3. Status enum (if you prefer for TidalViewData)
//enum class Status {
//    LOADING,
//    SUCCESS,
//    ERROR
//}
//
//// 4. TidalViewData (your UI data wrapper)
//class TidalViewData {
//    var status: Status = Status.LOADING
//    var document: TideModel? = null // Using TideModel now
//    var area: Area? = null // The selected area
//}