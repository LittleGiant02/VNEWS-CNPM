package com.cnpm.vnews.model

import com.squareup.moshi.Json

data class SearchBodyModel(
    @Json(name = "KeySearch")
    val keySearch: String? = null,
    @Json(name = "Tag")
    val tag: String? = null,
    @Json(name = "Page")
    val page: Int? = null,
    @Json(name = "Size")
    val Size: Int? = null,
)