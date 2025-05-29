package com.example.qweather.view_models.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qweather.repository.NotificationRepository
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationRepository: NotificationRepository) : ViewModel() {
    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList: LiveData<List<Notification>> = _notificationList

    fun getNotifications(page:Int) {
        viewModelScope.launch {
            try {
                val data = notificationRepository.getNotifications(page)
                _notificationList.value = data
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error: ${e.message}")
            }
        }
    }
}


