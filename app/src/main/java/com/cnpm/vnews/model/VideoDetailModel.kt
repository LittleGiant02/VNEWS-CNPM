package com.cnpm.vnews.model

import com.cnpm.vnews.model.ImageModel
import com.cnpm.vnews.model.MetadataModel
import com.squareup.moshi.Json

data class VideoDetailModel(
    @Json(name = "Path")
    val path: String? = null,

    @Json(name = "Keyword")
    val keyword: String? = null,

    @Json(name = "Duration")
    val duration: String? = null,

    @Json(name = "PrivateID")
    val privateID: String? = null,

    @Json(name = "Metadata")
    val metadata: List<MetadataModel>? = null,

    @Json(name = "Image")
    val image: List<ImageModel>? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "ID")
    val id: String? = null,

    @Json(name = "KeySearch")
    val keySearch: String? = null,

    @Json(name = "Schedule")
    val schedule: String? = null,

    @Json(name = "CreateDate")
    val createDate: String? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "MediaProjectID")
    val mediaProjectID: String? = null,


    @Json(name = "Body")
    val body: String? = null,


    @Json(name = "GroupCode")
    val groupCode: Any? = null,


    )