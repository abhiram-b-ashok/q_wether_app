package com.example.qweather.view_models.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.repository.ActivityRepository
import kotlinx.coroutines.launch

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {

    private val _activityList = MutableLiveData<NetworkResult<List<ActivityModel>>>()
    val activityList: LiveData<NetworkResult<List<ActivityModel>>> = _activityList

    fun getActivities() {
        _activityList.value = NetworkResult.Loading()
        viewModelScope.launch {
            val result = repository.getActivities()
            _activityList.value = result
        }
    }
}





