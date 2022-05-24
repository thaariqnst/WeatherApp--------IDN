package com.thaariq.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.thaariq.weatherapp.BuildConfig
import com.thaariq.weatherapp.databinding.ActivityMainBinding
import com.thaariq.weatherapp.utils.HelperFunctions.formatterDegree
import com.thaariq.weatherapp.utils.LOCATION_PERMISSION_REQ_CODE
import com.thaariq.weatherapp.utils.iconSizeWeather4x

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var _viewModel : ViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private val weatherAdapter by lazy { WeatherAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = true

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel= ViewModelProvider(this)[MainViewModel::class.java]

        searchCity()
        getWeatherByCity()


        viewModel.getForecastByCity().observe(this){
            weatherAdapter.setData(it.list)
            binding.rvForecastWeather.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = weatherAdapter
            }
        }

        getCurrentWeatherLocation()
    }

    private fun getWeatherByCity(){
        viewModel.getWeatherByCity().observe(this){
            binding.tvCity.text = it.name
            binding.tvDegree.text = formatterDegree(it.main?.temp)

            val icon = it.weather?.get(0)?.icon
            val iconUrl = BuildConfig.ICON_URL + icon + iconSizeWeather4x
            Glide.with(this).load(iconUrl).into(binding.imgIcWeather)
        }
    }

    private fun getCurrentWeatherLocation() {
        val fusedLocationClient : FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQ_CODE
                )
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                val lat = it.latitude
                val lon = it.longitude
            }
            .addOnFailureListener {
                Log.e("MainActivity", "FusedLocationError : Failed Getting current Location")
            }


        viewModel.getWeatherCurrentLocation().observe(this){
            binding.tvCity.text = it.name
            binding.tvDegree.text = formatterDegree(it.main?.temp)

            val icon = it.weather?.get(0)?.icon
            val iconUrl = BuildConfig.ICON_URL + icon + iconSizeWeather4x
            Glide.with(this).load(iconUrl).into(binding.imgIcWeather)
        }
    }

    private fun searchCity() {
        binding.edtSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.weatherByCity(it)
                        viewModel.forecastByCity(it)
                    }
                    try {
                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    }catch (e : Throwable){
                        Log.e("MainActivity", "hideSoftWIndow $e")
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }

            }
        )
    }
}