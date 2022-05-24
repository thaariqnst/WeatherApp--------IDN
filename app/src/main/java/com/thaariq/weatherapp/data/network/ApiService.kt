package com.thaariq.weatherapp.data.network

import com.thaariq.weatherapp.BuildConfig
import com.thaariq.weatherapp.data.ForecastResponse
import com.thaariq.weatherapp.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    fun weatherByCity(
        @Query("q") city : String,
        @Query("appid")apiKey: String? = BuildConfig.API_KEY
    ) : Call<WeatherResponse>

    @GET("forecast")
    fun forecastByCity(
        @Query("q") city : String,
        @Query("appid")apiKey: String? = BuildConfig.API_KEY
    ) : Call<ForecastResponse>

    @GET("weather")
    fun weatherByCurrentLocation(
        @Query("lat")lat : Double,
        @Query("lon")lon : Double,
        @Query("appid")apiKey: String? = BuildConfig.API_KEY
    ) : Call<WeatherResponse>

    @GET("forecast")
    fun forecastByCurrentLocation(
        @Query("lat")lat : Double,
        @Query("lon")lon : Double,
        @Query("appid")apiKey: String? = BuildConfig.API_KEY
    ) : Call<ForecastResponse>
}