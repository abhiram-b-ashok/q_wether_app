package com.example.qweather.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDashBoardBinding
import com.example.qweather.ui.side_nav_fragments.about_us.AboutUsFragment
import com.example.qweather.ui.side_nav_fragments.default_dashboard.city_bottom_sheet.CityBottomSheetFragment
import com.google.android.material.navigation.NavigationView


class DashBoardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashBoardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(requireActivity(),drawerLayout,R.string.nav_open,R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        navigationView = binding.navDrawer
        val navHeader = navigationView.getHeaderView(0)
        val closeButton = navHeader.findViewById<ImageView>(R.id.close)
        closeButton.setOnClickListener {
            drawerLayout.closeDrawer(binding.navDrawer)
        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.dash_box_fragment_container) as NavHostFragment
        navController = navHostFragment.navController


        binding.navToggler.setOnClickListener {
            drawerLayout.openDrawer(binding.navDrawer)
        }

        binding.navDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.defaultDashboardFragment -> {
                    navController.navigate(R.id.defaultDashboardFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }

                R.id.weatherNewsFragment -> {
                    navController.navigate(R.id.weatherNewsFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.rainRadarFragment -> {
                    navController.navigate(R.id.rainRadarFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.activitiesFragment -> {
                    navController.navigate(R.id.activitiesFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.weatherMapFragment -> {
                    navController.navigate(R.id.weatherMapFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.notificationsFragment -> {
                    navController.navigate(R.id.notificationsFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.monthlyReportFragment -> {
                    navController.navigate(R.id.monthlyReportFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.worldWideCitiesFragment -> {
                    navController.navigate(R.id.worldWideCitiesFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.aboutUsFragment -> {
                    navController.navigate(R.id.aboutUsFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.contactUsFragment -> {
                    navController.navigate(R.id.contactUsFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.disclaimerFragment -> {
                    navController.navigate(R.id.disclaimerFragment)
                    drawerLayout.closeDrawer(binding.navDrawer)
                    true
                }
                R.id.share_app -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://github.com/abhiram-b-ashok/q_wether_app")
                        type = "text/html"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)

                    true
                }

                else -> {

                    false
                }
            }

        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


}