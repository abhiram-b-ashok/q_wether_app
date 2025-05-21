package com.example.qweather.ui.side_nav_fragments.notifications_center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.databinding.FragmentNotificationsBinding
import com.example.qweather.repository.NotificationRepository
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationAdapter
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationModel
import com.example.qweather.view_models.notifications.NotificationViewModel


class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private val viewModel: NotificationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationViewModel(NotificationRepository()) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notificationList.observe(viewLifecycleOwner) {
            notificationAdapter = NotificationAdapter(it)
            binding.notificationsRecycler.adapter = notificationAdapter
        }

        viewModel.getNotifications()
    }
}
