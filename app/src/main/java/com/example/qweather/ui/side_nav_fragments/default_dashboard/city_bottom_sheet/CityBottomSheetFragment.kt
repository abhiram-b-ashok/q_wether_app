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
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import com.example.qweather.view_models.cities.CityViewModel
import com.example.qweather.view_models.cities.CityViewModelFactory
import com.example.qweather.view_models.city_search.CitySearchViewModel
import com.example.qweather.view_models.city_search.CitySearchViewModelFactory
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

//class CityBottomSheetFragment : BottomSheetDialogFragment() {
//    private lateinit var binding: FragmentCityBottomSheetBinding
//    private lateinit var qatarAdapter: QatarAdapter
//    private lateinit var worldAdapter: WorldAdapter
//    private lateinit var viewModel: CityViewModel
//    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
//    private lateinit var fused: FusedLocationProviderClient
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var db: FavoriteCityDatabase
//    private lateinit var dao: FavoriteCitiesDao
//    private val locationRequest = LocationRequest.Builder(
//        Priority.PRIORITY_HIGH_ACCURACY, 10000L
//    ).setMinUpdateIntervalMillis(5000L).build()
//
//
//    private val sharedPrefs by lazy {
//        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCityBottomSheetBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        db = FavoriteCityDatabase.getDatabase(requireContext())
//        dao = db.favoriteCitiesDao()
//
//        fused = LocationServices.getFusedLocationProviderClient(requireContext())
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val location = locationResult.lastLocation
//                if (location != null) {
//                    val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
//                    binding.tvLocation.text = getCityName(location.latitude, location.longitude)
//                    Log.d("LocationCallback", text)
//                } else {
//                    binding.tvLocation.text = "location null"
//                }
//            }
//        }
//
//        locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//            ) {
//                // Permissions granted, now try to get location and start updates
//                getLocation()
//                checkLocationSettingsAndStartUpdates()
//            } else {
//                binding.tvLocation.text = "Permission denied"
//                // Handle the case where permissions are denied.
//                // You might want to show a message to the user or disable location features.
//            }
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//
//        val repository = CitiesRepository(okHttpClient)
//        viewModel = ViewModelProvider(
//            this,
//            CityViewModelFactory(repository)
//        )[CityViewModel::class.java]
//
//        setupAdapters()
//        setupClickListeners()
//        observeViewModel()
//
//        viewModel.fetchCities()
//        loadSavedCityType()
//    }
//
//    private fun setupAdapters() {
//
//        qatarAdapter = QatarAdapter(mutableListOf()).apply {
//            onItemClickListener = { city ->
//                sendSelection(city.cityName, true, city.longitude, city.latitude, city.cityId)
//                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
//                dismiss()
//            }
//        }
//
//        worldAdapter = WorldAdapter(mutableListOf()).apply {
//            onItemClickListener = { city ->
//                sendSelection(city.cityName, false, city.longitude, city.latitude, city.cityId)
//                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
//                dismiss()
//            }
//
//            onStarClickListener = { city ->
//                CoroutineScope(Dispatchers.IO).launch {
//                    val existing = dao.getFavoriteCityById(city.cityId)
//                    if (existing != null) {
//                        dao.deleteFavoriteCity(existing)
//                        city.isSelected = false
//                    } else {
//                        val favorite = FavoriteCitiesModel(
//                            cityId = city.cityId,
//                            cityName = city.cityName,
//                            temperature = "",
//                            weatherType = "",
//                            date = "",
//                            isSaved = true
//                        )
//                        dao.insertFavoriteCity(favorite)
//                        city.isSelected = true
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        worldAdapter.notifyDataSetChanged()
//                    }
//                }
//            }
//        }
//
//        binding.locationsRecyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = qatarAdapter
//        }
//    }
//
//    private fun setupClickListeners() {
//        binding.apply {
//            qatarButton.setOnClickListener { selectedType(CityType.QATAR) }
//            worldwideButton.setOnClickListener { selectedType(CityType.WORLD) }
//            backButton.setOnClickListener { dismiss() }
//        }
//    }
//
//    private fun observeViewModel() {
//        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is NetworkResult.Loading -> showLoading(true)
//                is NetworkResult.Success -> {
//                    showLoading(false)
//                    result.data?.let { response ->
//                        updateAdapters(response)
//                    }
//                }
//
//                is NetworkResult.Error -> {
//                    showLoading(false)
//                    showError(result.message ?: getString(R.string.error_unknown))
//                }
//            }
//        }
//    }
//
//    private fun updateAdapters(response: CitiesResponse) {
//        response.response.result.cities.let { cities ->
//            qatarAdapter.updateList(
//                cities.qatar.map { QatarCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId) }
//            )
//            worldAdapter.updateList(
//                cities.world.map { WorldCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId) }
//            )
//        }
//    }
//
//    private fun selectedType(type: CityType) {
//        binding.apply {
//            val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
//            val whiteColor = Color.WHITE
//            when (type) {
//                CityType.QATAR -> {
//                    qatarLabel.setTextColor(primaryColor)
//                    worldLabel.setTextColor(whiteColor)
//                    qatarButtonLayout.setBackgroundColor(whiteColor)
//                    worldwideButtonLayout.setBackgroundColor(primaryColor)
//                    locationsRecyclerView.adapter = qatarAdapter
//                    locationType.text = getString(R.string.qatar_cities)
//                }
//
//                CityType.WORLD -> {
//                    worldLabel.setTextColor(primaryColor)
//                    qatarLabel.setTextColor(whiteColor)
//                    worldwideButtonLayout.setBackgroundColor(whiteColor)
//                    qatarButtonLayout.setBackgroundColor(primaryColor)
//                    locationsRecyclerView.adapter = worldAdapter
//                    locationType.text = getString(R.string.worldwide_cities)
//                }
//            }
//        }
//    }
//
//    private fun showLoading(show: Boolean) {
//        binding.apply {
//            progressBar.visibility = if (show) View.VISIBLE else View.GONE
//            locationsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
//        }
//    }
//
//    private fun showError(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun sendSelection(
//        cityName: String,
//        isQatar: Boolean,
//        latitude: Double,
//        longitude: Double,
//        cityId: Int
//    ) {
//        parentFragmentManager.setFragmentResult(
//            "CITY_SELECTION_RESULT",
//            bundleOf(
//                "SELECTED_CITY" to cityName,
//                "IS_QATAR" to isQatar,
//                "LATITUDE" to latitude,
//                "LONGITUDE" to longitude,
//                "CITY_ID" to cityId
//            )
//        )
//        dismiss()
//    }
//
//    private fun loadSavedCityType() {
//        if (!sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)) {
//            selectedType(CityType.WORLD)
//        } else {
//            selectedType(CityType.QATAR)
//        }
//    }
//
//    private fun requestPermissions() {
//        locationPermissionRequest.launch(
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        )
//    }
//
//    private fun getLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            binding.tvLocation.text = "Permission not granted for getLocation"
//            // This should ideally not be reached if requestPermissions() is handled correctly.
//            return
//        }
//
//        fused.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
//                binding.tvLocation.text =  getCityName(location.latitude,location.longitude)
//                Log.d("LastLocation", text)
//            } else {
//                binding.tvLocation.text = "Last location not found"
//
//            }
//        }.addOnFailureListener { e ->
//            binding.tvLocation.text = "Failed to get last location"
//            Log.e("LastLocation", "Error getting last location", e)
//        }
//    }
//
//    private fun checkLocationSettingsAndStartUpdates() {
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//            .setAlwaysShow(true)
//
//        val client = LocationServices.getSettingsClient(requireContext())
//        val task = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener {
//            // Location settings are satisfied. Check permissions again before starting updates.
//            if (ActivityCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                startLocationUpdates()
//            } else {
//                // This state should ideally be handled by the permission request flow.
//                Log.w("Permissions", "Location permissions not granted when trying to start updates after settings check.")
//                binding.tvLocation.text = "Permission needed for updates"
//            }
//        }
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    // Using a member for the request code for clarity.
//                    startIntentSenderForResult(exception.resolution.intentSender, LOCATION_SETTINGS_REQUEST_CODE, null, 0, 0, 0, null)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                    Log.e("SettingsClient", "Failed to show location settings dialog: ${sendEx.message}")
//                }
//            } else {
//                binding.tvLocation.text = "Location settings are not satisfied. Turn on location."
//                Log.e("SettingsClient", "Location settings are not satisfied: ${exception.message}")
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // Check permissions first. If granted, proceed to check settings and start updates.
//        // If not granted, requestPermissions will handle it.
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            getLocation() // Get last known location
//            checkLocationSettingsAndStartUpdates() // Then check settings and start updates
//        } else {
//            // Request permissions if not already granted.
//            // The ActivityResultLauncher callback will handle the outcome.
//            requestPermissions()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // Stop location updates when the Fragment is not visible
//        fused.removeLocationUpdates(locationCallback)
//    }
//
//    @RequiresPermission(
//        anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
//    )
//
//    private fun startLocationUpdates() {
//        // Double-check permissions before requesting updates, although checkLocationSettingsAndStartUpdates should ensure this.
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.w("Permissions", "Attempted to start location updates without permission.")
//            return
//        }
//        try {
//            fused.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//            Log.d("LocationUpdates", "Requested location updates.")
//        } catch (e: SecurityException) {
//            Log.e("LocationUpdates", "SecurityException while requesting location updates.", e)
//            binding.tvLocation.text = "Permission error for updates"
//        }
//    }
//    private fun getCityName(lat: Double,long: Double):String{
//        val cityName: String?
//        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
//        val Adress = geoCoder.getFromLocation(lat,long,3)
//
//        cityName = Adress?.get(0)?.locality
//
//        return cityName.toString()
//    }
//
//
//    companion object {
//        private const val LOCATION_SETTINGS_REQUEST_CODE = 1001
//    }
//}
//
//enum class CityType {
//    QATAR, WORLD
//}

//class CityBottomSheetFragment : BottomSheetDialogFragment() {
//    private lateinit var binding: FragmentCityBottomSheetBinding
//    private lateinit var qatarAdapter: QatarAdapter
//    private lateinit var worldAdapter: WorldAdapter
//    private lateinit var viewModel: CityViewModel
//    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
//    private lateinit var fused: FusedLocationProviderClient
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var db: FavoriteCityDatabase
//    private lateinit var dao: FavoriteCitiesDao
//    private lateinit var citySearchViewModel: CitySearchViewModel
//    private val locationRequest = LocationRequest.Builder(
//        Priority.PRIORITY_HIGH_ACCURACY, 10000L
//    ).setMinUpdateIntervalMillis(5000L).build()
//
//
//    private val sharedPrefs by lazy {
//        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCityBottomSheetBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val citySearchRepository = CitySearchRepository()
//        val citySearchViewModelFactory = CitySearchViewModelFactory(citySearchRepository)
//        citySearchViewModel = ViewModelProvider(this, citySearchViewModelFactory).get(
//            CitySearchViewModel::class.java)
//
//
//        db = FavoriteCityDatabase.getDatabase(requireContext())
//        dao = db.favoriteCitiesDao()
//
//
//        fused = LocationServices.getFusedLocationProviderClient(requireContext())
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val location = locationResult.lastLocation
//                if (location != null) {
//                    val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
//                    binding.tvLocation.text = getCityName(location.latitude, location.longitude)
//                    Log.d("LocationCallback", text)
//                } else {
//                    binding.tvLocation.text = "location null"
//                }
//            }
//        }
//        binding.apply {
//            citySearchBarLayout.setOnClickListener {
//                citySearchBarEditText.requestFocus()
//                citySearchBarEditText.showKeyboard()
//            }
//        }
//
//
//
//        locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//            ) {
//                getLocation()
//                checkLocationSettingsAndStartUpdates()
//            } else {
//                binding.tvLocation.text = "Permission denied"
//
//            }
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//
//        val repository = CitiesRepository(okHttpClient)
//        viewModel = ViewModelProvider(
//            this,
//            CityViewModelFactory(repository)
//        )[CityViewModel::class.java]
//
//        setupAdapters()
//        setupClickListeners()
//        observeViewModel()
//        observeSearchViewModel()
//
//        viewModel.fetchCities()
//        loadSavedCityType()
//        binding.apply {
//
//            binding.citySearchBarEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    citySearchViewModel.searchCities(s.toString())
//                }
//            })
//        }
//    }
//
//    private fun setupAdapters() {
//
//        qatarAdapter = QatarAdapter(mutableListOf()).apply {
//            onItemClickListener = { city ->
//                sendSelection(city.cityName, true, city.longitude, city.latitude, city.cityId)
//                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
//                dismiss()
//            }
//        }
//
//        worldAdapter = WorldAdapter(mutableListOf()).apply {
//            onItemClickListener = { city ->
//                sendSelection(city.cityName, false, city.longitude, city.latitude, city.cityId)
//                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
//                dismiss()
//            }
//
//
//            onStarClickListener = { city ->
//                CoroutineScope(Dispatchers.IO).launch {
//                    val existing = dao.getFavoriteCityById(city.cityId)
//                    if (existing != null) {
//                        dao.deleteFavoriteCity(existing)
//                        city.isSelected = false
//                    } else {
//                        val favorite = FavoriteCitiesModel(
//                            cityId = city.cityId,
//                            cityName = city.cityName,
//                            latitude = city.latitude,
//                            longitude = city.longitude,
//                            isSaved = true
//                        )
//                        dao.insertFavoriteCity(favorite)
//                        city.isSelected = true
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        worldAdapter.notifyDataSetChanged()
//                    }
//                }
//            }
//
//        }
//
//        binding.locationsRecyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = qatarAdapter
//        }
//    }
//
//    private fun setupClickListeners() {
//        binding.apply {
//            qatarButton.setOnClickListener { selectedType(CityType.QATAR) }
//            worldwideButton.setOnClickListener { selectedType(CityType.WORLD) }
//            backButton.setOnClickListener { dismiss() }
//        }
//    }
//
//    private fun observeViewModel() {
//        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is NetworkResult.Loading -> showLoading(true)
//                is NetworkResult.Success -> {
//                    showLoading(false)
//                    result.data?.let { response ->
//                        updateAdapters(response)
//                    }
//                }
//
//                is NetworkResult.Error -> {
//                    showLoading(false)
//                    showError(result.message ?: getString(R.string.error_unknown))
//                }
//            }
//        }
//    }
//
//    private fun observeSearchViewModel() {
//        citySearchViewModel.citySearchResponse.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is NetworkResult.Success -> {
//                    val worldCityList = mapSearchResultsToWorldCities(result.data)
//                    worldAdapter.updateList(worldCityList)
//                }
//                is NetworkResult.Error -> {
//                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
//                }
//                else -> {}
//            }
//        }
//    }
//
//
//    private fun updateAdapters(response: CitiesResponse) {
//        response.response.result.cities.let { cities ->
//            qatarAdapter.updateList(
//                cities.qatar.map { QatarCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId) }
//            )
//        }
//    }
//
//    private fun selectedType(type: CityType) {
//        binding.apply {
//            val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
//            val whiteColor = Color.WHITE
//            when (type) {
//                CityType.QATAR -> {
//                    qatarLabel.setTextColor(primaryColor)
//                    worldLabel.setTextColor(whiteColor)
//                    qatarButtonLayout.setBackgroundColor(whiteColor)
//                    worldwideButtonLayout.setBackgroundColor(primaryColor)
//                    locationsRecyclerView.adapter = qatarAdapter
//                    locationType.text = getString(R.string.qatar_cities)
//                    citySearchBar.visibility = View.GONE
//                }
//
//                CityType.WORLD -> {
//                    worldLabel.setTextColor(primaryColor)
//                    qatarLabel.setTextColor(whiteColor)
//                    worldwideButtonLayout.setBackgroundColor(whiteColor)
//                    qatarButtonLayout.setBackgroundColor(primaryColor)
//                    locationsRecyclerView.adapter = worldAdapter
//                    locationType.text = getString(R.string.worldwide_cities)
//                    citySearchBar.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//
//    private fun showLoading(show: Boolean) {
//        binding.apply {
//            progressBar.visibility = if (show) View.VISIBLE else View.GONE
//            locationsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
//        }
//    }
//
//    private fun showError(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun sendSelection(
//        cityName: String,
//        isQatar: Boolean,
//        latitude: Double,
//        longitude: Double,
//        cityId: Int
//    ) {
//        parentFragmentManager.setFragmentResult(
//            "CITY_SELECTION_RESULT",
//            bundleOf(
//                "SELECTED_CITY" to cityName,
//                "IS_QATAR" to isQatar,
//                "LATITUDE" to latitude,
//                "LONGITUDE" to longitude,
//                "CITY_ID" to cityId
//            )
//        )
//        dismiss()
//    }
//
//    private fun loadSavedCityType() {
//        if (!sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)) {

enum class CityType {
    QATAR, WORLD
}


fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


class CityBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCityBottomSheetBinding
    private lateinit var qatarAdapter: QatarAdapter
    private lateinit var worldAdapter: WorldAdapter
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
        citySearchViewModel.citySearchResponse.observe(viewLifecycleOwner) { result ->
            if (result is NetworkResult.Success) {
                lifecycleScope.launch {
                    val favoriteIds = getFavoriteCityIds()
                    val mapped = mapToWorldCities(result.data, favoriteIds)
                    worldCityList.clear()
                    worldCityList.addAll(mapped)
                    worldAdapter.notifyDataSetChanged()
                }
            } else if (result is NetworkResult.Error) {
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
            }

            CityType.WORLD -> {
                binding.worldLabel.setTextColor(primary)
                binding.qatarLabel.setTextColor(white)
                binding.worldwideButtonLayout.setBackgroundColor(white)
                binding.qatarButtonLayout.setBackgroundColor(primary)
                binding.locationsRecyclerView.adapter = worldAdapter
                binding.locationType.text = "Worldwide Cities"
                binding.citySearchBar.visibility = View.VISIBLE
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



    companion object {
        private const val LOCATION_SETTINGS_REQUEST_CODE = 1001
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

}


