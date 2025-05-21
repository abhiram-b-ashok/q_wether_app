package com.example.qweather.ui.side_nav_fragments.weather_news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qweather.R
import com.example.qweather.data.models.weather_news.WeatherNewsModel
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentWeatherNewsBinding
import com.example.qweather.repository.WeatherNewsRepository
import com.example.qweather.ui.side_nav_fragments.weather_news.adapter.WeatherNewsAdapter
import com.example.qweather.view_models.weather_news.WeatherNewsViewModel


class WeatherNewsFragment : Fragment() {
    private lateinit var binding: FragmentWeatherNewsBinding
    private lateinit var viewModel: WeatherNewsViewModel
    private lateinit var adapter: WeatherNewsAdapter
    private val newsList = arrayListOf<WeatherNewsModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = arrayListOf(
            WeatherNewsModel("https://static.zawya.com/view/acePublic/alias/contentid/MDI0NzYzMDEtNGMyMi00/12/2026763638.webp?f=3%3A2&q=0.75&w=3840","Qatar: Hot, humid with some clouds weather expected today","August 19, 2024"),
            WeatherNewsModel("https://www.qatar-tribune.com/uploads/imported_images/upload/latestnews/8219/ad5c7c04-8239-48f1-b945-a1e0fff51913.jpg","Unstable weather conditions expected from today: QMD", "December 29, 2021"),
            WeatherNewsModel("https://metbeatnews.com/wp-content/uploads/2024/02/1000152223.jpg","Qatar Meteorology forecasts thunder, rain with strong wind for weekend","August 1, 2024")

        )


        adapter = WeatherNewsAdapter(list)
        binding.newsRecycler.adapter = adapter



//        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return WeatherNewsViewModel(WeatherNewsRepository()) as T
//            }
//        })[WeatherNewsViewModel::class.java]
//
//        viewModel.weatherNewsList.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is NetworkResult.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//
//                is NetworkResult.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    val items = result.data ?: emptyList()
//                    newsList.clear()
//                    newsList.addAll(items)
//                    adapter.notifyDataSetChanged()
//                }
//
//                is NetworkResult.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(requireContext(), result.message ?: "Error occurred", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        viewModel.getWeatherNews()
    }
}
