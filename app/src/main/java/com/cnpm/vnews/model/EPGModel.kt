package com.cnpm.vnews.model

import com.squareup.moshi.Json

data class EPGModel (
    @Json(name = "ID")
    val id: Long? = null,

    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "ServiceID")
    val serviceID: Long? = null,

    @Json(name = "StartTime")
    val startTime: String? = null,

    @Json(name = "EndTime")
    val endTime: String? = null,

    @Json(name = "Duration")
    val duration: Long? = null,

    @Json(name = "CurrentEvent")
    val currentEvent: Boolean? = null,

    @Json(name = "Url")
    val url: String? = null
)