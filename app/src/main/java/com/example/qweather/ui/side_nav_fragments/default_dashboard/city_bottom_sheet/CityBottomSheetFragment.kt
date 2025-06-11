package com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet

//import android.Manifest
//import android.content.Context
//import android.content.IntentSender
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.location.LocationRequest
//import android.os.Bundle
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.result.launch
//import androidx.annotation.RequiresPermission
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.qweather.databinding.FragmentCityBottomSheetBinding
//import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
//import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
//import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
//import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
//import androidx.core.os.bundleOf
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.qweather.R
//import com.example.qweather.data.models.cities.CitiesResponse
//import com.example.qweather.data.network.NetworkResult
//import com.example.qweather.repository.CitiesRepository
//import com.example.qweather.view_models.cities.CityViewModel
//import com.example.qweather.view_models.cities.CityViewModelFactory
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.LocationSettingsRequest
//import com.google.android.gms.location.Priority
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//
//class CityBottomSheetFragment : BottomSheetDialogFragment() {
//    private lateinit var binding: FragmentCityBottomSheetBinding
//    private lateinit var qatarAdapter: QatarAdapter
//    private lateinit var worldAdapter: WorldAdapter
//    private lateinit var viewModel: CityViewModel
//    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
//    private lateinit var fused: FusedLocationProviderClient
//    private lateinit var locationCallback: LocationCallback
//    private val locationRequest = LocationRequest.Builder(
//        Priority.PRIORITY_HIGH_ACCURACY, 10000L
//    ).setMinUpdateIntervalMillis(5000L).build()
//
//
//
//    private val sharedPrefs by lazy {
//        requireContext().getSharedPreferences("CityPrefs", Context.MODE_PRIVATE)
//    }
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
//        requestPermissions()
//        fused = LocationServices.getFusedLocationProviderClient(requireContext())
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val location = locationResult.lastLocation
//                if (location != null) {
//                    val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
//                    binding.tvLocation.text = text
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
//                getLocation()
//            } else {
//                binding.tvLocation.text = "Permission denied"
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
//                city.isSelected = !city.isSelected
//
//                notifyDataSetChanged()
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
//            when (type) {
//                CityType.QATAR -> {
//                    qatarLabel.setTextColor(primaryColor)
//                    worldLabel.setTextColor(Color.WHITE)
//                    qatarButtonLayout.setBackgroundColor(Color.WHITE)
//                    worldwideButtonLayout.setBackgroundColor(primaryColor)
//                    locationsRecyclerView.adapter = qatarAdapter
//                    locationType.text = getString(R.string.qatar_cities)
//                }
//                CityType.WORLD -> {
//                    worldLabel.setTextColor(primaryColor)
//                    qatarLabel.setTextColor(Color.WHITE)
//                    worldwideButtonLayout.setBackgroundColor(Color.WHITE)
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
//    private fun sendSelection(cityName: String, isQatar: Boolean, latitude: Double, longitude: Double, cityId:Int) {
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
//    private fun loadSavedCityType(){
//        if(!sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true))
//        {
//            selectedType(CityType.WORLD)
//        }
//        else
//        {
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
//            binding.tvLocation.text = "Permission not granted"
//            return
//        }
//
//        fused.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
//                binding.tvLocation.text = text
//                Log.d("LastLocation", text)
//            } else {
//                binding.tvLocation.text = "Location not found"
//            }
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
//            if (ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                startLocationUpdates()
//            } else {
//                Log.w("Permissions", "Location permissions not granted at startLocationUpdates")
//            }
//        }
//
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                try {
//                    exception.startResolutionForResult(this, 1001)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    Log.e("SettingsClient", "Failed to show dialog: ${sendEx.message}")
//                }
//            } else {
//                binding.tvLocation.text = "Location settings are not satisfied."
//            }
//        }
//    }
//
//
//
//    override fun onResume() {
//        super.onResume()
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(
//                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            checkLocationSettingsAndStartUpdates()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        fused.removeLocationUpdates(locationCallback)
//    }
//    @RequiresPermission(
//        allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
//    )
//
//    private fun startLocationUpdates() {
//        fused.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//}
//
//enum class CityType {
//    QATAR, WORLD
//}

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qweather.R
import com.example.qweather.data.models.cities.CitiesResponse
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.data.room_database.FavoriteCitiesModel
import com.example.qweather.data.room_database.FavoriteCityDatabase
import com.example.qweather.databinding.FragmentCityBottomSheetBinding
import com.example.qweather.repository.CitiesRepository
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.qatar_adapter.QatarCitiesModel
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldAdapter
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.adapters.world_adapter.WorldCitiesModel
import com.example.qweather.view_models.cities.CityViewModel
import com.example.qweather.view_models.cities.CityViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
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
    private lateinit var viewModel: CityViewModel
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fused: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var db: FavoriteCityDatabase
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
        val dao = db.favoriteCitiesDao()

        fused = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
                    binding.tvLocation.text = getCityName(location.latitude, location.longitude)
                    Log.d("LocationCallback", text)
                } else {
                    binding.tvLocation.text = "location null"
                }
            }
        }

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Permissions granted, now try to get location and start updates
                getLocation()
                checkLocationSettingsAndStartUpdates()
            } else {
                binding.tvLocation.text = "Permission denied"
                // Handle the case where permissions are denied.
                // You might want to show a message to the user or disable location features.
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val repository = CitiesRepository(okHttpClient)
        viewModel = ViewModelProvider(
            this,
            CityViewModelFactory(repository)
        )[CityViewModel::class.java]

        setupAdapters()
        setupClickListeners()
        observeViewModel()

        viewModel.fetchCities()
        loadSavedCityType()
    }

    private fun setupAdapters() {

        qatarAdapter = QatarAdapter(mutableListOf()).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, true, city.longitude, city.latitude, city.cityId)
                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
                dismiss()
            }
        }

        worldAdapter = WorldAdapter(mutableListOf()).apply {
            onItemClickListener = { city ->
                sendSelection(city.cityName, false, city.longitude, city.latitude, city.cityId)
                Log.e("@@@@@longitude,latitude", "${city.longitude},${city.latitude}")
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
                            temperature = "",  // fetched later
                            weatherType = "",  // fetched later
                            date = "",         // fetched later
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

        binding.locationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = qatarAdapter // Default adapter
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            qatarButton.setOnClickListener { selectedType(CityType.QATAR) }
            worldwideButton.setOnClickListener { selectedType(CityType.WORLD) }
            backButton.setOnClickListener { dismiss() }
        }
    }

    private fun observeViewModel() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    result.data?.let { response ->
                        updateAdapters(response)
                    }
                }

                is NetworkResult.Error -> {
                    showLoading(false)
                    showError(result.message ?: getString(R.string.error_unknown))
                }
            }
        }
    }

    private fun updateAdapters(response: CitiesResponse) {
        response.response.result.cities.let { cities ->
            qatarAdapter.updateList(
                cities.qatar.map { QatarCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId) }
            )
            worldAdapter.updateList(
                cities.world.map { WorldCitiesModel(it.name, it, it.longitude, it.latitude, it.cityId) }
            )
        }
    }

    private fun selectedType(type: CityType) {
        binding.apply {
            val primaryColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
            val whiteColor = Color.WHITE // Cache white color for slight optimization

            when (type) {
                CityType.QATAR -> {
                    qatarLabel.setTextColor(primaryColor)
                    worldLabel.setTextColor(whiteColor)
                    qatarButtonLayout.setBackgroundColor(whiteColor)
                    worldwideButtonLayout.setBackgroundColor(primaryColor)
                    locationsRecyclerView.adapter = qatarAdapter
                    locationType.text = getString(R.string.qatar_cities)
                }

                CityType.WORLD -> {
                    worldLabel.setTextColor(primaryColor)
                    qatarLabel.setTextColor(whiteColor)
                    worldwideButtonLayout.setBackgroundColor(whiteColor)
                    qatarButtonLayout.setBackgroundColor(primaryColor)
                    locationsRecyclerView.adapter = worldAdapter
                    locationType.text = getString(R.string.worldwide_cities)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            locationsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
        dismiss()
    }

    private fun loadSavedCityType() {
        if (!sharedPrefs.getBoolean("LAST_CITY_IS_QATAR", true)) {
            selectedType(CityType.WORLD)
        } else {
            selectedType(CityType.QATAR)
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
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvLocation.text = "Permission not granted for getLocation"
            // This should ideally not be reached if requestPermissions() is handled correctly.
            return
        }

        fused.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val text = "latitude: ${location.latitude}, longitude: ${location.longitude}"
                binding.tvLocation.text =  getCityName(location.latitude,location.longitude)
                Log.d("LastLocation", text)
            } else {
                binding.tvLocation.text = "Last location not found"
                // It's possible for lastLocation to be null, especially on a new device or after clearing location data.
                // Location updates (if started) will provide a more current location.
            }
        }.addOnFailureListener { e ->
            binding.tvLocation.text = "Failed to get last location"
            Log.e("LastLocation", "Error getting last location", e)
        }
    }

    private fun checkLocationSettingsAndStartUpdates() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true) // This can be intrusive; consider if it's always necessary.

        val client = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location settings are satisfied. Check permissions again before starting updates.
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startLocationUpdates()
            } else {
                // This state should ideally be handled by the permission request flow.
                Log.w("Permissions", "Location permissions not granted when trying to start updates after settings check.")
                binding.tvLocation.text = "Permission needed for updates"
            }
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    // Using a member for the request code for clarity.
                    startIntentSenderForResult(exception.resolution.intentSender, LOCATION_SETTINGS_REQUEST_CODE, null, 0, 0, 0, null)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    Log.e("SettingsClient", "Failed to show location settings dialog: ${sendEx.message}")
                }
            } else {
                binding.tvLocation.text = "Location settings are not satisfied. Turn on location."
                Log.e("SettingsClient", "Location settings are not satisfied: ${exception.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check permissions first. If granted, proceed to check settings and start updates.
        // If not granted, requestPermissions will handle it.
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation() // Get last known location
            checkLocationSettingsAndStartUpdates() // Then check settings and start updates
        } else {
            // Request permissions if not already granted.
            // The ActivityResultLauncher callback will handle the outcome.
            requestPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop location updates when the Fragment is not visible
        fused.removeLocationUpdates(locationCallback)
    }

    @RequiresPermission(
        anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION]
    )
    private fun startLocationUpdates() {
        // Double-check permissions before requesting updates, although checkLocationSettingsAndStartUpdates should ensure this.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("Permissions", "Attempted to start location updates without permission.")
            return
        }
        try {
            fused.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Log.d("LocationUpdates", "Requested location updates.")
        } catch (e: SecurityException) {
            Log.e("LocationUpdates", "SecurityException while requesting location updates.", e)
            binding.tvLocation.text = "Permission error for updates"
        }
    }
    private fun getCityName(lat: Double,long: Double):String{
        val cityName: String?
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress?.get(0)?.locality

        return cityName.toString()
    }


    companion object {
        private const val LOCATION_SETTINGS_REQUEST_CODE = 1001
    }
}

enum class CityType {
    QATAR, WORLD
}


