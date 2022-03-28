package com.example.listingapp

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherModel(
    val weather : List<Weather>,
    val main : Main,
    val wind : Wind,
    val name : String
)
data class Weather(
    val description : String
)
data class Main(
    val temp : String,
    val humidity : String
)
data class Wind (
    val speed : String
    )

