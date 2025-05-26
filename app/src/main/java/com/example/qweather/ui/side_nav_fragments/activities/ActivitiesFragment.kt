package com.example.qweather.ui.side_nav_fragments.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.R
import com.example.qweather.data.models.activities.ActivityModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentActivitiesBinding
import com.example.qweather.databinding.FragmentNotificationsBinding
import com.example.qweather.repository.ActivityRepository
import com.example.qweather.ui.side_nav_fragments.activities.adapter.ActivityAdapter
import com.example.qweather.view_models.activities.ActivityViewModel



class ActivitiesFragment : Fragment() {
    private lateinit var binding: FragmentActivitiesBinding
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var viewModel: ActivityViewModel
    private val activityList = arrayListOf<ActivityModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityAdapter = ActivityAdapter(activityList)
        binding.activitiesRecycler.adapter = activityAdapter

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActivityViewModel(ActivityRepository()) as T
            }
        })[ActivityViewModel::class.java]

        viewModel.activityList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    activityList.clear()
                    activityList.addAll(result.data ?: emptyList())
                    activityAdapter.notifyDataSetChanged()
                    Log.d("TAG", "onViewCreated: ${result.data}")
                    binding.progressBar.visibility = View.GONE
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Error", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.getActivities()
    }

}
