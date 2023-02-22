package com.cnpm.vnews.const_util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.activity.addCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.cnpm.vnews.R
import com.cnpm.vnews.model.EPGModel
import com.cnpm.vnews.model.ImageModel
import com.cnpm.vnews.model.MetadataModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val domainShare = "https://vnews.gov.vn/news/"

const val Slug: String = "Slug"
const val DVBCategories: String = "DVBCategories"
const val ContentType: String = "ContentType"
const val TIMER_DELAY_MILLISECONDS: Long = 2500
const val TIMER_DELAY_MILLISECONDS_SLIDER: Long = 5000

@SuppressLint("SimpleDateFormat")
val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")



fun getPositionLive(listEPG: List<EPGModel>): Int {
    var positionLive = 0
    listEPG.forEachIndexed { index, epgModel ->
        val timeStart = dateFormatter.parse(epgModel.startTime!!)
        val timeEnd = dateFormatter.parse(epgModel.endTime!!)
        val calendarStart = Calendar.getInstance()
        calendarStart.time = timeStart!!
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = timeEnd!!

        val calendarStart2: Calendar?
        val indexNext: Int
        if (index >= listEPG.size - 1) {
            indexNext = listEPG.size - 1
            val dateStart2 = dateFormatter.parse(listEPG[indexNext].startTime!!)
            calendarStart2 = Calendar.getInstance()
            calendarStart2.time = dateStart2!!
        } else {
            indexNext = index + 1
            val dateStart2 = dateFormatter.parse(listEPG[indexNext].startTime!!)
            calendarStart2 = Calendar.getInstance()
            calendarStart2.time = dateStart2!!
        }

        val calendarNow = Calendar.getInstance()
        calendarNow.time = Date()
        if ((calendarNow.time.after(calendarStart.time) && calendarNow.time.before(calendarEnd.time)) || (calendarNow.time.after(calendarEnd.time)
            && calendarNow.time.before(calendarStart2.time))) {
            positionLive = index
        }
    }
    return positionLive
}


fun isVideoRenderer(
    mappedTrackInfo: MappingTrackSelector.MappedTrackInfo,
    rendererIndex: Int
): Boolean {
    val trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
    if (trackGroupArray.length == 0) {
        return false
    }
    val trackType = mappedTrackInfo.getRendererType(rendererIndex)
    return C.TRACK_TYPE_VIDEO == trackType
}

fun backPressDispatcher(requireActivity: FragmentActivity, viewLifecycleOwner: LifecycleOwner) {
    requireActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        requireActivity.supportFragmentManager.popBackStack()
    }
}


val optionsLoadImage: RequestOptions = RequestOptions()
    .centerCrop()
    .placeholder(R.drawable.image_default)
    .error(R.drawable.image_default)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .priority(Priority.HIGH)


fun shareLink(context: Context, slug: String, title: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vnews CNPM")
        val shareMessage: String = domainShare + slug.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, title))
    } catch (e: Exception) {
        return e.printStackTrace()
    }
}


fun getUrlImageForType(listImage: List<ImageModel>, type: String): String? {
    var url: String? = null
    if (listImage.isEmpty()) {
        return ""
    }
    listImage.forEach {
        if (it.type == type) {
            url = it.Cdn + it.url
        }
    }
    if (url == null || url == "") {
        url = listImage.first().Cdn + listImage.first().url
    }
    return url
}

fun getValueMetadataForName(listMetadata: List<MetadataModel>, name: String): String {
    var value: Any? = null
    listMetadata.forEach {
        if (it.name == name) {
            value = it.value
        }
    }
    return if (value.toString() == "" || value.toString() == "null") {
        "VNews"
    } else {
        value.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun getTimeAgo(schedule: String, resource: Resources): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val date = dateFormatter.parse(schedule)
    val diff = abs(Date().time - date!!.time)
    val diffMinutes: Long = diff / (60 * 1000)
    val diffHours: Long = diff / (60 * 60 * 1000)
    val diffDays: Long = diff / (60 * 60 * 1000 * 24)
    val diffMonths = (diff / (60 * 60 * 1000 * 24 * 30.41666666))
    val diffYears: Long = diff / (60.toLong() * 60 * 1000 * 24 * 365)

    return when {
        diffYears >= 1 -> resource.getString(R.string.years_before, diffYears.toInt())
        diffMonths >= 1 -> resource.getString(R.string.months_before, diffMonths.toInt())
        diffDays >= 1 -> resource.getString(R.string.days_before, diffDays.toInt())
        diffHours >= 1 -> resource.getString(R.string.hours_before, diffHours.toInt())
        diffMinutes >= 1 -> resource.getString(R.string.minutes_before, diffMinutes.toInt())
        else -> resource.getString(R.string.minutes_before, 1)
    }
}

fun convertDuration(duration: String): String {
    return duration.substring(0, 8)
}
