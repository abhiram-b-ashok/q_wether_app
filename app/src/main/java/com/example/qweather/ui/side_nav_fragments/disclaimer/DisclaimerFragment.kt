package com.example.qweather.ui.side_nav_fragments.disclaimer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.R
import com.example.qweather.databinding.FragmentDisclaimerBinding


class DisclaimerFragment : Fragment() {
    private lateinit var binding: FragmentDisclaimerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisclaimerBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            wwwCo.setOnClickListener {
                val url = "https://www.openweathermap.co.uk"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            wwwAbout.setOnClickListener {
                val url = "https://www.openweathermap.co.uk/about"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            wwwOrg.setOnClickListener {
                val url = "https://www.openweathermap.org"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
            }

        }
    }


}