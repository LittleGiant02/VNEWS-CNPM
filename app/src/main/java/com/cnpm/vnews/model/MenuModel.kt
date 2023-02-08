package com.cnpm.vnews.model

import com.squareup.moshi.Json

data class MenuModel(
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

    @Json(name = "LayoutType")
    val layoutType: LayoutType? = null,

    @Json(name = "CDN")
    val cdn: CDN? = null,

    @Json(name = "OrderID")
    val orderID: Long? = null,

    @Json(name = "Components")
    val components: List<Component>? = null,

    @Json(name = "Media")
    val media: List<MediaModel>? = null,

    @Json(name = "Domain")
    val domain: String? = null,
)

data class CDN(
    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "LiveDomain")
    val liveDomain: String? = null,

    @Json(name = "VideoDomain")
    val videoDomain: String? = null,

    @Json(name = "ImageDomain")
    val imageDomain: String? = null,

    @Json(name = "IsActive")
    val isActive: Boolean? = null
)

data class Component(
    @Json(name = "ID")
    val id: Long? = null,

    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "PrivateKey")
    val privateKey: String? = null,

    @Json(name = "Count")
    val count: Long? = null,

    @Json(name = "OrderID")
    val orderID: Long? = null,

    @Json(name = "Domain")
    val domain: String? = null,

    @Json(name = "Components")
    val components: String? = null,

    @Json(name = "LayoutType")
    val layoutType: LayoutType? = null,

    @Json(name = "Icon")
    val icon: String? = null,

    @Json(name = "Logo")
    val logo: Any? = null,

    @Json(name = "Url")
    val url: String? = null,

    @Json(name = "Schedule")
    val schedule: Any? = null,

    @Json(name = "Description")
    val description: String? = null
)

data class LayoutType(
    @Json(name = "Type")
    val type: Long? = null,

    @Json(name = "SubType")
    val subType: Long? = null,

    @Json(name = "Landscape")
    val landscape: Landscape? = null,

    @Json(name = "Portrait")
    val portrait: Landscape? = null
)

data class Landscape(
    @Json(name = "Height")
    val height: Long? = null,

    @Json(name = "Width")
    val width: Long? = null
)

data class MediaModel(
    @Json(name = "ID")
    val id: String? = null,

    @Json(name = "PrivateID")
    val privateID: String? = null,

    @Json(name = "Description")
    val description: String? = null,

    @Json(name = "Image")
    val image: List<ImageModel>? = null,

    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "Title")
    val title: String? = null,

    @Json(name = "Path")
    val path: String? = null,

    @Json(name = "Schedule")
    val schedule: String? = null,

    @Json(name = "Duration")
    val duration: String? = null,

    @Json(name = "Metadata")
    val metadata: List<MetadataModel>? = null
)

data class ImageModel(
    @Json(name = "Type")
    val type: String? = null,

    @Json(name = "Url")
    val url: String? = null,

    @Json(name = "Cdn")
    val Cdn: String? = null
)

data class MetadataModel(
    @Json(name = "Name")
    val name: String? = null,

    @Json(name = "Value")
    val value: Any? = null,
)


