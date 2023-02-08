package com.cnpm.vnews.model

import com.squareup.moshi.Json

class CategoryItemModel(
    @Json(name = "PrivateKey")
    val privateKey: String? = null,

    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "Color")
    val color: String? = null,

    @Json(name = "Style")
    val style: Any? = null,

    @Json(name = "Icon")
    val icon: String? = null,

    @Json(name = "Logo")
    val logo: Any? = null,

    @Json(name = "LayoutType")
    val layoutType: LayoutType? = null,

    @Json(name = "CDN")
    val cdn: CDN? = null,

    @Json(name = "Seo")
    val seo: SEO? = null,

    @Json(name = "Url")
    val url: String? = null,

    @Json(name = "Domain")
    val domain: String? = null,

    @Json(name = "OrderID")
    val orderID: Long? = null,

    @Json(name = "Components")
    val components: List<Component>? = null,

    @Json(name = "Media")
    val media: List<MediaModel>? = null
)


data class SEO(
    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "Url")
    val url: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "Key")
    val key: String? = null
)
