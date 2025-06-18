package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qweather.R
import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.models.city_search.CitySearchModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.room_database.FavoriteCitiesDao
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.data.room_database.FavoriteCityDatabase
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.repository.CitySearchRepository
import com.example.qweather.repository.ForecastRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.saved_cities.FavoriteCitiesAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import com.example.qweather.utility_funtions.hideKeyboard
import com.example.qweather.utility_funtions.showKeyboard
import com.example.qweather.view_models.cities.CityViewModel
import com.example.qweather.view_models.cities.CityViewModelFactory
import com.example.qweather.view_models.city_search.CitySearchViewModel
import com.example.qweather.view_models.city_search.CitySearchViewModelFactory
import com.example.qweather.view_models.forecast.ForecastViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Locale

class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter
    private lateinit var favoriteCitiesAdapter: FavoriteCitiesAdapter
    private lateinit var viewModel: CityViewModel
    private lateinit var citySearchViewModel: CitySearchViewModel
    private lateinit var fused: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var db: FavoriteCityDatabase
    private lateinit var dao: FavoriteCitiesDao

    private val worldCityList = mutableListOf<WorldCitiesModel>()

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 10000L
    ).setMinUpdateIntervalMillis(5000L).build()

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FavoriteCityDatabase.getDatabase(requireContext())
        dao = db.favoriteCitiesDao()

        fused = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    binding.tvLocation.text = getCityName(loc.latitude, loc.longitude)
                } ?: run {
                    binding.tvLocation.text = "Location null"
                }
            }
        }

        binding.citySearchBarLayout.setOnClickListener {
            binding.citySearchBarEditText.requestFocus()
            binding.citySearchBarEditText.showKeyboard()
        }

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                getLocation()
                checkLocationSettingsAndStartUpdates()
            } else {
                binding.tvLocation.text = "Permission denied"
            }
        }

        citySearchViewModel = ViewModelProvider(
            this,
            CitySearchViewModelFactory(CitySearchRepository())
        )[CitySearchViewModel::class.java]

        viewModel = ViewModelProvider(
            this,
            CityViewModelFactory(CitiesRepository(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()))
        )[CityViewModel::class.java]

        setupAdapters()
        setupClickListeners()
        observeViewModel()
        observeSearchViewModel()

        viewModel.fetchCities()
        loadSavedCityType()

        favoriteCitiesAdapter = FavoriteCitiesAdapter(emptyList())
        binding.favoritesRecyclerView.adapter = favoriteCitiesAdapter
        favoriteCitiesAdapter.onItemClickListener ={
            sendSelection(it.cityName, false, it.latitude, it.longitude, it.cityId)
            dismiss()
        }

        binding.citySearchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                if (input.isNotEmpty()) {
                    citySearchViewModel.searchCities(input)
                } else {
                    worldCityList.clear()
                    worldAdapter.notifyDataSetChanged()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        loadFavorites()
    }

    private fun setupAdapters() {
        qatarAdapter = QatarAdapter(mutableListOf()).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, true, city.longitude, city.latitude, city.cityId)
                dismiss()
            }
        }

        worldAdapter = WorldAdapter(worldCityList).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, false, city.longitude, city.latitude, city.cityId)
                dismiss()
            }
                        onStarClickListener = { city ->
                CoroutineScope(Dispatchers.IO).launch {
                    val existing = dao.getFavoriteCityById(city.cityId)

                    if (existing != null) {
                        dao.deleteFavoriteCity(existing)
                        city.isSelected = false
                    } else {
                        val favorite = FavoriteCitiesModel(
                            cityId = city.cityId,
                            cityName = city.cityName,
                            latitude = city.latitude,
                            longitude = city.longitude,
                            isSaved = true
                        )
                        dao.insertFavoriteCity(favorite)
                        city.isSelected = true
                    }

                    withContext(Dispatchers.Main) {
                        worldAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        binding.locationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.locationsRecyclerView.adapter = qatarAdapter
    }

    private fun setupClickListeners() {
        binding.qatarButton.setOnClickListener { selectedType(CityType.QATAR) }
        binding.worldwideButton.setOnClickListener { selectedType(CityType.WORLD) }
        binding.backButton.setOnClickListener { dismiss() }
    }

    private fun observeViewModel() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    result.data?.let { updateAdapters(it) }
                }

                is NetworkResult.Error -> {
                    showLoading(false)
                    showError(result.message ?: "Unknown error")
                }
            }
        }
    }

    private fun observeSearchViewModel() {
        citySearchViewModel.citySearchResponse.observe(viewLifecycleOwner) {  result ->
            if (result is NetworkResult.Success) {
                lifecycleScope.launch {
                    val favoriteIds = getFavoriteCityIds()
                    val mapped = mapToWorldCities(result.data, favoriteIds)
                    worldCityList.clear()
                    worldCityList.addAll(mapped)
                    worldAdapter.notifyDataSetChanged()
                    showLoading(false)
                    binding.citySearchBarEditText.hideKeyboard()

                }

            } else if(result is NetworkResult.Loading)
            {
                showLoading(true)
            }

            else if (result is NetworkResult.Error) {
                Toast.makeText(requireContext(), result.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAdapters(response: CitiesResponse) {
        response.response.result.cities.qatar.let { qatar ->
            qatarAdapter.updateList(
                qatar.map {
                    QatarCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId)
                }
            )
        }
    }

    private fun selectedType(type: CityType) {
        val primary = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        val white = Color.WHITE
        when (type) {
            CityType.QATAR -> {
                binding.qatarLabel.setTextColor(primary)
                binding.worldLabel.setTextColor(white)
                binding.qatarButtonLayout.setBackgroundColor(white)
                binding.worldwideButtonLayout.setBackgroundColor(primary)
                binding.locationsRecyclerView.adapter = qatarAdapter
                binding.locationType.text = "Qatar Cities"
                binding.citySearchBar.visibility = View.GONE
                binding.favoritesRecyclerView.visibility = View.GONE
            }

            CityType.WORLD -> {
                binding.worldLabel.setTextColor(primary)
                binding.qatarLabel.setTextColor(white)
                binding.worldwideButtonLayout.setBackgroundColor(white)
                binding.qatarButtonLayout.setBackgroundColor(primary)
                binding.locationsRecyclerView.adapter = worldAdapter
                binding.locationType.text = "Worldwide Cities"
                binding.citySearchBar.visibility = View.VISIBLE
                binding.favoritesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.locationsRecyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun sendSelection(
        cityName: String,
        isQatar: Boolean,
        latitude: Double,
        longitude: Double,
        cityId: Int
    ) {
        parentFragmentManager.setFragmentResult(
            "CITY_SELECTION_RESULT",
            bundleOf(
                "SELECTED_CITY" to cityName,
                "IS_QATAR" to isQatar,
                "LATITUDE" to latitude,
                "LONGITUDE" to longitude,
                "CITY_ID" to cityId
            )
        )
    }

    private fun loadSavedCityType() {
        if (sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)) {
            selectedType(CityType.QATAR)
        } else {
            selectedType(CityType.WORLD)
        }
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getLocation() {
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvLocation.text = "Permission not granted"
            return
        }

        fused.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                binding.tvLocation.text = getCityName(location.latitude, location.longitude)
            } else {
                binding.tvLocation.text = "Last location not found"
            }
        }.addOnFailureListener {
            binding.tvLocation.text = "Failed to get last location"
        }
    }

    private fun checkLocationSettingsAndStartUpdates() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(requireContext())
        client.checkLocationSettings(builder.build()).addOnSuccessListener {
            if (
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                startLocationUpdates()
            }
        }.addOnFailureListener {
            binding.tvLocation.text = "Location settings error"
        }
    }

    override fun onResume() {
        super.onResume()
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
            checkLocationSettingsAndStartUpdates()
        } else {
            requestPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        fused.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) return

        fused.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun getCityName(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = geocoder.getFromLocation(lat, lon, 1)
            address?.firstOrNull()?.locality ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private suspend fun getFavoriteCityIds(): Set<Int> = withContext(Dispatchers.IO) {
        dao.getAllFavoriteCities().map { it.cityId }.toSet()
    }

    private fun mapToWorldCities(
        results: List<CitySearchModel>?,
        favoriteIds: Set<Int>
    ): List<WorldCitiesModel> {
        return results?.map { city ->
            WorldCitiesModel(
                cityName = city.name,
                cityId = city.cityId,
                latitude = city.lat,
                longitude = city.lon,
                isSelected = favoriteIds.contains(city.cityId)
            )
        } ?: emptyList()
    }

    private fun loadFavorites() {

        lifecycleScope.launch(Dispatchers.IO) {
            val favorites = dao.getAllFavoriteCities()
            withContext(Dispatchers.Main) {
                favoriteCitiesAdapter.updateList(favorites)
            }
        }
    }
}
enum class CityType {
    QATAR, WORLD
}







