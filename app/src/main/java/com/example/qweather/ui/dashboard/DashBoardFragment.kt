package com.example.qweather.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDashBoardBinding


class DashBoardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle



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
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)'
        binding.navToggler.setOnClickListener {
            drawerLayout.openDrawer(binding.navDrawer)
        }
        binding.navDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.close_icon -> {
                    drawerLayout.closeDrawer(binding.navDrawer)
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