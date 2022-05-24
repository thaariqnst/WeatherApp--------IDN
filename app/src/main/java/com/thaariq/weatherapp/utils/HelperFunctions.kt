package com.thaariq.weatherapp.utils

import java.math.RoundingMode

object HelperFunctions {

    fun formatterDegree(temperature : Double?) : String{
        val temp =  temperature as Double
        val tempToCelsius = temp - 273.0
        val formatDegree = tempToCelsius.toBigDecimal().setScale(2, RoundingMode.CEILING)
        return "$formatDegree°C"
    }
}