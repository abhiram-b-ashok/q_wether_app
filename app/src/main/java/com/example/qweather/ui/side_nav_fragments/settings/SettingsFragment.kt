package com.example.qweather.ui.side_nav_fragments.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.R
import com.example.qweather.data.models.settings.DashboardSettingsModel
import com.example.qweather.databinding.FragmentSettingsBinding
import com.example.qweather.ui.side_nav_fragments.settings.adapter.SettingsAdapter
import java.util.Collections
import kotlin.text.split

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsAdapter: SettingsAdapter
    private lateinit var list: ArrayList<DashboardSettingsModel>

    var selectedLanguage = 1
    var selectedTemperature = "°C"
    var selectedWind = "km/h"
    var selectedTide = "m"

    private val PREF_NAME = "settingPreference"
    private val KEY_SELECTED_DASHBOARD_ITEMS = "selectedDashboardItems"
    private val KEY_ORDERED_TITLES = "dashboardOrderString"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences =
            requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        selectedLanguage = sharedPreferences.getInt("selectedLanguage", 1)
        selectLanguage(selectedLanguage)

        selectedTemperature = sharedPreferences.getString("selectedTemperature", "°C").toString()
        selectTemperature(selectedTemperature)

        selectedWind = sharedPreferences.getString("selectedWind", "km/h").toString()
        selectWind(selectedWind)

        selectedTide = sharedPreferences.getString("selectedTide", "m").toString()
        selectTide(selectedTide)


        binding.apply {
            englishButton.setOnClickListener {
                selectLanguage(1)
                selectedLanguage = 1
                sharedPreferences.edit().putInt("selectedLanguage", selectedLanguage).apply()
            }
            arabicButton.setOnClickListener {
                selectLanguage(2)
                selectedLanguage = 2
                sharedPreferences.edit().putInt("selectedLanguage", selectedLanguage).apply()
            }
            celciusButton.setOnClickListener {
                selectTemperature("°C")
                selectedTemperature = "°C"
                sharedPreferences.edit().putString("selectedTemperature", selectedTemperature).apply()
            }
            fahrenheitButton.setOnClickListener {
                selectTemperature("F")
                selectedTemperature = "F"
                sharedPreferences.edit().putString("selectedTemperature", selectedTemperature).apply()
            }
            kelvinButton.setOnClickListener {
                selectTemperature("K")
                selectedTemperature = "K"
                sharedPreferences.edit().putString("selectedTemperature", selectedTemperature).apply()
            }
            kmButton.setOnClickListener {
                selectWind("km/h")
                selectedWind = "km/h"
                sharedPreferences.edit().putString("selectedWind", selectedWind).apply()
            }
            knotButton.setOnClickListener {
                selectWind("kt")
                selectedWind = "kt"
                sharedPreferences.edit().putString("selectedWind", selectedWind).apply()
            }
            msButton.setOnClickListener {
                selectWind("m/s")
                selectedWind = "m/s"
                sharedPreferences.edit().putString("selectedWind", selectedWind).apply()
            }
            mphButton.setOnClickListener {
                selectWind("mph")
                selectedWind = "mph"
                sharedPreferences.edit().putString("selectedWind", selectedWind).apply()
            }
            mButton.setOnClickListener {
                selectTide("m")
                selectedTide = "m"
                sharedPreferences.edit().putString("selectedTide", selectedTide).apply()
            }
            ftButton.setOnClickListener {
                selectTide("ft")
                selectedTide = "ft"
                sharedPreferences.edit().putString("selectedTide", selectedTide).apply()
            }


            val sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

// Load previously selected titles
            val selectedItemsSet = sharedPreferences.getStringSet(KEY_SELECTED_DASHBOARD_ITEMS, HashSet()) ?: HashSet()

// Base dashboard list (default order)
            val defaultList = arrayListOf(
                DashboardSettingsModel(R.drawable.default_selected_ic, "Current Weather"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Warning"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Forecast"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Sunrise / Sunset info"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Moon phase"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Tidal information"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Rain Radar"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Weather Map"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Seasonal"),
                DashboardSettingsModel(R.drawable.to_select_ic, "Marine Forecast"),
            )

// Load saved order
            val savedOrderString = sharedPreferences.getString(KEY_ORDERED_TITLES, null)
            list = if (!savedOrderString.isNullOrEmpty()) {
                val savedOrder = savedOrderString.split(",")
                val orderedList = mutableListOf<DashboardSettingsModel>()

                // Add items in saved order
                savedOrder.forEach { title ->
                    defaultList.find { it.title == title }?.let { orderedList.add(it) }
                }

                // Add new items that weren’t saved before
                defaultList.forEach { item ->
                    if (orderedList.none { it.title == item.title }) {
                        orderedList.add(item)
                    }
                }

                ArrayList(orderedList)
            } else {
                defaultList
            }

// Apply selected state
            list.forEach { item ->
                item.isSelect = selectedItemsSet.contains(item.title)
                item.toggleImage = if (item.isSelect || item.title == "Current Weather") {
                    R.drawable.default_selected_ic
                } else {
                    R.drawable.to_select_ic
                }
            }
            val currentSelectedTitles = list.filter { it.isSelect }.map { it.title }.toSet()
            sharedPreferences.edit().putStringSet(KEY_SELECTED_DASHBOARD_ITEMS, currentSelectedTitles).apply()

            settingsAdapter = SettingsAdapter(list)
            binding.recyclerForSettings.adapter = settingsAdapter

            settingsAdapter.onItemSelect = { selectedModel ->

                // Prevent unselecting "Current Weather"
                if (selectedModel.title != "Current Weather") {
                    selectedModel.isSelect = !selectedModel.isSelect
                    selectedModel.toggleImage =
                        if (selectedModel.isSelect) R.drawable.selected_ic else R.drawable.to_select_ic
                    settingsAdapter.notifyDataSetChanged()

                    val selectedTitles =
                        list.filter { it.isSelect || it.title == "Current Weather" }
                            .map { it.title }.toSet()
                    sharedPreferences.edit()
                        .putStringSet(KEY_SELECTED_DASHBOARD_ITEMS, selectedTitles).apply()
                }
            }



            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    source: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val sourcePosition = source.adapterPosition
                    val targetPosition = target.adapterPosition
                    Collections.swap(list, sourcePosition, targetPosition)
                    settingsAdapter.notifyItemMoved(sourcePosition, targetPosition)
                    // Save reordered titles as comma-separated string
                    val newOrderString = list.joinToString(",") { it.title }
                    sharedPreferences.edit().putString(KEY_ORDERED_TITLES, newOrderString).apply()

                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    TODO("Not yet implemented")
                }
            })
            itemTouchHelper.attachToRecyclerView(binding.recyclerForSettings)

        }
    }


    private fun selectLanguage(language: Int) {
        binding.apply {
            if (language == 1) {
                englishLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                arabicLabel.setTextColor(resources.getColor(R.color.white))
                englishButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                arabicButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            } else {
                arabicLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                englishLabel.setTextColor(resources.getColor(R.color.white))
                arabicButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                englishButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
        }
    }

    private fun selectTemperature(temperature: String) {
        binding.apply {
            if (temperature == "°C") {
                celciusLabel.setTextColor(resources.getColor(R.color.maroon))
                fahrenheitLabel.setTextColor(resources.getColor(R.color.white))
                kelvinLabel.setTextColor(resources.getColor(R.color.white))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            } else if (temperature == "F") {
                fahrenheitLabel.setTextColor(resources.getColor(R.color.maroon))
                celciusLabel.setTextColor(resources.getColor(R.color.white))
                kelvinLabel.setTextColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))

            } else {
                kelvinLabel.setTextColor(resources.getColor(R.color.maroon))
                fahrenheitLabel.setTextColor(resources.getColor(R.color.white))
                celciusLabel.setTextColor(resources.getColor(R.color.white))
                kelvinButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                fahrenheitButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
                celciusButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            }
        }
    }

    private fun selectWind(wind: String) {
        binding.apply {
            if (wind == "km/h") {
                kmLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            } else if (wind == "kt") {
                knotLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            } else if (wind == "m/s") {
                msLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                mphLabel.setTextColor(resources.getColor(R.color.white))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            } else {
                mphLabel.setTextColor(resources.getColor(R.color.darkmaroon))
                kmLabel.setTextColor(resources.getColor(R.color.white))
                knotLabel.setTextColor(resources.getColor(R.color.white))
                msLabel.setTextColor(resources.getColor(R.color.white))
                mphButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                kmButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                knotButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
                msButtonLayout.setBackgroundColor(resources.getColor(R.color.darkmaroon))
            }
        }
    }

    private fun selectTide(tide: String) {

        binding.apply {
            if (tide == "m") {
                mLabel.setTextColor(resources.getColor(R.color.maroon))
                ftLabel.setTextColor(resources.getColor(R.color.white))
                mButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                ftButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))
            } else {
                ftLabel.setTextColor(resources.getColor(R.color.maroon))
                mLabel.setTextColor(resources.getColor(R.color.white))
                ftButtonLayout.setBackgroundColor(resources.getColor(R.color.white))
                mButtonLayout.setBackgroundColor(resources.getColor(R.color.maroon))

            }
        }
    }


}
