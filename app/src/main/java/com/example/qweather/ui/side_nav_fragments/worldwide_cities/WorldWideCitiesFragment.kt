package com.example.qweather.ui.side_nav_fragments.worldwide_cities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qweather.data.room_database.FavoriteCitiesDao
import com.example.qweather.data.room_database.FavoriteCityDatabase
import com.example.qweather.databinding.FragmentWorldWideCitiesBinding
import com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter.FavoriteCitiesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WorldWideCitiesFragment : Fragment() {
    private lateinit var binding: FragmentWorldWideCitiesBinding
    private lateinit var adapter: FavoriteCitiesAdapter
    private lateinit var dao: FavoriteCitiesDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorldWideCitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FavoriteCityDatabase.getDatabase(requireContext())
        dao = FavoriteCityDatabase.getDatabase(requireContext()).favoriteCitiesDao()

        adapter = FavoriteCitiesAdapter(emptyList()).apply {
            onRemoveClickListener = { city ->
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteFavoriteCity(city)
                    loadFavorites()
                }
            }
        }

        binding.worldCitiesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.worldCitiesRecycler.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favorites = dao.getAllFavoriteCities()
            withContext(Dispatchers.Main) {
                adapter.updateList(favorites)
            }
        }
    }
}


