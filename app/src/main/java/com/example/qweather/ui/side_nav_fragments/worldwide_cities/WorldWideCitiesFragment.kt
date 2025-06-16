package com.example.qweather.ui.side_nav_fragments.worldwide_cities

import android.R
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qweather.data.room_database.FavoriteCitiesDao
import com.example.qweather.data.room_database.FavoriteCityDatabase
import com.example.qweather.databinding.FragmentWorldWideCitiesBinding
import com.example.qweather.repository.WeatherRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.CityBottomSheetFragment
import com.example.qweather.ui.side_nav_fragments.worldwide_cities.adapter.FavoriteCitiesAdapter
import com.example.qweather.view_models.city_weather.WeatherViewModel
import com.example.qweather.view_models.city_weather.WeatherViewModelFactory
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorldWideCitiesFragment : Fragment() {
    private lateinit var binding: FragmentWorldWideCitiesBinding
    private lateinit var adapter: FavoriteCitiesAdapter
    private lateinit var dao: FavoriteCitiesDao
    private lateinit var cityBottomSheetFragment: CityBottomSheetFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorldWideCitiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = FavoriteCityDatabase.getDatabase(requireContext()).favoriteCitiesDao()

        adapter = FavoriteCitiesAdapter(emptyList())

        binding.worldCitiesRecycler.adapter = adapter
        binding.addLocationButton.setOnClickListener {
            cityBottomSheetFragment = CityBottomSheetFragment()
            cityBottomSheetFragment.show(childFragmentManager, "CityBottomSheetFragment")
        }


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Get the city to delete from the adapter's list based on position
                // You'll need a method in your adapter to get the item at a specific position
                val cityToDelete =
                    adapter.getItemAt(position) // Assuming you add this method to your adapter

                cityToDelete?.let { city ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        dao.deleteFavoriteCity(city)
                        loadFavorites() // Reload data after deletion
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.holo_red_dark
                        )
                    )
                    .addActionIcon(R.drawable.ic_menu_delete)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.worldCitiesRecycler)

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

