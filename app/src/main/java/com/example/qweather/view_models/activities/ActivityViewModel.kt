package com.example.qweather.view_models.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.repository.ActivityRepository

class ActivityViewModel(activityRepository: ActivityRepository): ViewModel() {
    private val _activityList = MutableLiveData<List<ActivityModel>>()
    val activityList: LiveData<List<ActivityModel>> = _activityList




}