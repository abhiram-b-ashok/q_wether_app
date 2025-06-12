package com.example.qweather.ui.side_nav_fragments.notifications_center

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.databinding.FragmentNotificationsBinding
import com.example.qweather.repository.NotificationRepository
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationAdapter
import com.example.qweather.view_models.notifications.NotificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private var page = 1

    private val allNotifications = mutableListOf<Notification>()

    private val viewModel: NotificationViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationViewModel(NotificationRepository()) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(3500)
            binding.progressBar.visibility = View.GONE
        }

        notificationAdapter = NotificationAdapter(mutableListOf())
        binding.notificationsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
        viewModel.getNotifications(page)

        viewModel.notificationList.observe(viewLifecycleOwner) { notificationList ->
            allNotifications.addAll(notificationList)
            notificationAdapter.updateData(allNotifications)
        }

        binding.seeMore.setOnClickListener {
            page++
            viewModel.getNotifications(page)
        }
    }
}


