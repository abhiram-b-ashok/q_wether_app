//package com.example.qweather.view_models.tides
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.qweather.data.models.tide.NetworkResult
//import com.example.qweather.data.models.tide.Status
//import com.example.qweather.data.models.tide.TidalViewData
//import com.example.qweather.repository.TideRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.util.Date
//
//class TidesViewModel(private val tidesRepository: TideRepository) : ViewModel() {
//
//    private val _tideViewState = MutableStateFlow(TidalViewData()) // Or LiveData
//    val tideViewState: StateFlow<TidalViewData> = _tideViewState.asStateFlow()
//
//    fun fetchTides(areaId: String) {
//        viewModelScope.launch {
//            _tideViewState.value = _tideViewState.value.copy(status = Status.LOADING) // Assuming TidalViewData can be a data class or has copy method
//            val response = tidesRepository.fetchTidesApi()
//
//            when (response) {
//                is NetworkResult.Success -> {
//                    val tideDataDocument = response.data
//                    if (tideDataDocument != null) {
//                        val selectedArea = tideDataDocument.areas.firstOrNull { it.id == areaId }
//                        _tideViewState.update { currentState ->
//                            currentState.copy(
//                                status = Status.SUCCESS,
//                                document = tideDataDocument.toTideXmlDocument(), // If you still need the original structure in TidalViewData
//                                area = selectedArea?.toTidalAreaTag(), // Convert back if TidalViewData expects TidalAreaTag
//                                // Calculate and set currentHeightMeters, maxTideHeightMeters etc. here
//                                lastUpdatedTime = Date() // Assuming you want current time
//                            )
//                        }
//                        // Now, if you need to calculate max/min, do it here using selectedArea.values
//                        // e.g., val maxVal = selectedArea.values.maxOfOrNull { it.value }
//                        // _tideViewState.update { it.copy(maxTideHeightMeters = maxVal) }
//                    } else {
//                        _tideViewState.update { it.copy(status = Status.ERROR, document = null, area = null, message = "No data received") }
//                    }
//                }
//                is NetworkResult.Error -> {
//                    _tideViewState.update { it.copy(status = Status.ERROR, document = null, area = null, message = response.message) }
//                }
//                is NetworkResult.Loading -> {
//                    // Handled above before the network call
//                }
//            }
//        }
//    }
//}