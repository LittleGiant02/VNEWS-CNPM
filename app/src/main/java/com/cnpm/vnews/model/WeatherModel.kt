package com.cnpm.vnews.model

import com.squareup.moshi.Json

data class WeatherModel (
    @Json(name = "Name")
    val name: String,

    @Json(name = "Data")
    val data: Data
)

data class Data (
    val currently: Currently,
    val daily: List<Daily>
)

data class Currently (
    val humidity: Double,
    val temperature: Double,
    val windSpeed: Double,
    val icon: String
)

data class Daily (
    val temperatureMin: Double,
    val temperatureMax: Double,
    val icon: String,
    val time: Long
)
