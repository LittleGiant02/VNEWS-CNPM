package com.cnpm.vnews.model

import com.cnpm.vnews.model.ImageModel
import com.squareup.moshi.Json

class RelatedModel(
    @Json(name = "ID")
    val id: String? = null,

    @Json(name = "PrivateID")
    val privateID: String? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "Slug")
    val slug: String? = null,

    @Json(name = "Image")
    val image: List<ImageModel>? = null,

    @Json(name = "ContentType")
    val contentType: String? = null
)
