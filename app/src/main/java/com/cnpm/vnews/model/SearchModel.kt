package com.cnpm.vnews.model

import com.cnpm.vnews.model.ImageModel
import com.cnpm.vnews.model.MetadataModel
import com.squareup.moshi.Json

data class SearchModel(
    @Json(name = "ID")
    val id: String ?= null,

    @Json(name = "Metadata")
    val metadata: List<MetadataModel>? = null,

    @Json(name = "MediaProjectID")
    val mediaProjectID: String? = null,

    @Json(name = "Image")
    val image: List<ImageModel>? = null,

    @Json(name = "PrivateID")
    val privateID: String? = null,

    @Json(name = "KeySearch")
    val keySearch: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "CreateDate")
    val createDate: String? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "Path")
    val path: String? = null,

    @Json(name = "Duration")
    val duration: String? = null,

    @Json(name = "Keyword")
    val keyword: String? = null,

    @Json(name = "Schedule")
    val schedule: String? = null,

    @Json(name = "Id")
    val welcome10ID: String? = null
)

data class SearchSuggestModel(
    @Json(name = "String")
    val titleSuggest: String? = null,
    @Json(name = "Score")
    val score: Double ? =null,
    @Json(name = "Payload")
    val payload: String? = null,
)
