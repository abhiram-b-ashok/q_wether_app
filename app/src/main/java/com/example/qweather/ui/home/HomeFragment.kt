package com.example.qweather.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}

//this is smy city bottom sheet fragment

//this is world cities fragment where i will show favorite cities in recycler view

//this is saved city data class

//this is saved city dao

//this is saved city database

//this is weather repository

//this is weather view model

//this is weather view model factory

//this is favorite/saved cities adapter

//this are the data classes i have used

//check this and