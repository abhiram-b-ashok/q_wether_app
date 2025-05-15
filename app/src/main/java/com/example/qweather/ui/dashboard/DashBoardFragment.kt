package com.example.qweather.ui.dashboard

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
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDashBoardBinding
import com.google.android.material.navigation.NavigationView


class DashBoardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView



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



        binding.navToggler.setOnClickListener {
            drawerLayout.openDrawer(binding.navDrawer)
        }
        binding.navDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.weather_news -> {
                    true
                }
                R.id.rain_radar -> {
                    true
                }
                R.id.activities -> {
                    true
                }
                R.id.weather_maps -> {
                    true
                }
                R.id.notifications_center -> {
                    true
                }
                R.id.monthly_report -> {
                    true
                }
                R.id.worldwide_cities -> {
                    true
                }
                R.id.about_us -> {
                    true
                }
                R.id.settings -> {
                    true
                }
                R.id.contact_us -> {
                    true
                }
                R.id.disclaimer -> {
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