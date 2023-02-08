package com.cnpm.vnews.network

import com.cnpm.vnews.const_util.apiCategory
import com.cnpm.vnews.const_util.apiDetail
import com.cnpm.vnews.const_util.apiSearch
import com.cnpm.vnews.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiClient {

    @GET("$apiCategory{privateKey}")
    suspend fun fetchDataMenu(@Path("privateKey") privateKey: String): MenuModel

    @GET("$apiCategory{privateKey}")
    suspend fun fetchDataCategoryItem(@Path("privateKey") privateKey: String): CategoryItemModel

    @GET("$apiDetail{privateId}")
    suspend fun fetchDataVideoNewsDetail(@Path("privateId") id: String): VideoDetailModel

    @GET("${apiCategory}vnews_epg_{date}")
    suspend fun fetchEPG(@Path("date") date: String): List<EPGModel>

    @GET("$apiCategory{privateKey}")
    suspend fun fetchDataHighlight(@Path("privateKey") privateKey: String): List<MediaModel>

    @GET("$apiCategory{privateKey}")
    suspend fun fetchDataWeather(@Path("privateKey") privateKey: String): List<WeatherModel>

    @POST(apiSearch)
    suspend fun fetchDataSearch(@Body searchBody: SearchBodyModel): List<SearchModel>

}