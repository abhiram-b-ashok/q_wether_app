//package com.example.qweather.ui.side_nav_fragments.notifications_center
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.qweather.databinding.FragmentNotificationsBinding
//import com.example.qweather.repository.NotificationRepository
//import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.Notification
//import com.example.qweather.ui.side_nav_fragments.notifications_center.adapter.NotificationAdapter
//import com.example.qweather.view_models.notifications.NotificationViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//
//class NotificationsFragment : Fragment() {
//
//    private lateinit var binding: FragmentNotificationsBinding
//    private lateinit var notificationAdapter: NotificationAdapter
//    private var page = 1
//
//    private val allNotifications = mutableListOf<Notification>()
//
//    private val viewModel: NotificationViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return NotificationViewModel(NotificationRepository()) as T
//            }
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launch {
//            delay(2500)
//            binding.progressBar.visibility = View.GONE
//        }
//
//        notificationAdapter = NotificationAdapter(mutableListOf())
//        binding.notificationsRecycler.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = notificationAdapter
//        }
//        viewModel.getNotifications(page)
//
//        viewModel.notificationList.observe(viewLifecycleOwner) { notificationList ->
//            allNotifications.addAll(notificationList)
//            notificationAdapter.updateData(allNotifications)
//        }
//        binding.seeMore.setOnClickListener {
//            page++
//            viewModel.getNotifications(page)
//
//
//        }
//    }
//}
//

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
    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

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

        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            delay(1000)
        }

        setupRecyclerView()
        observeViewModel()

        loadMoreNotifications()
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(mutableListOf())
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = notificationAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0

                        ) {
                            loadMoreItems()
                        }
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        viewModel.notificationList.observe(viewLifecycleOwner) { newNotifications ->
            if (currentPage == 1 && binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.GONE
            }
            binding.progressBar.visibility = View.GONE
            isLoading = false

            if (newNotifications.isNotEmpty()) {
                allNotifications.addAll(newNotifications)
                notificationAdapter.addData(newNotifications)
            } else {
                if (currentPage > 1 || allNotifications.isEmpty()) {
                    isLastPage = true
                }
            }
        }

    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        binding.progressBar.visibility = View.VISIBLE
        loadMoreNotifications()
    }

    private fun loadMoreNotifications() {
        if (currentPage == 1 && allNotifications.isEmpty() && !isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        }
        viewModel.getNotifications(currentPage)
    }
}
