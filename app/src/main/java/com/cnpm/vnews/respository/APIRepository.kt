package com.cnpm.vnews.respository

import com.cnpm.vnews.model.*
import com.cnpm.vnews.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class APIRepository {
    companion object {
        suspend fun getDataMenu(privateKey: String): Flow<MenuModel> = flow {
            val response = ApiService.api.fetchDataMenu(privateKey)
            emit(response)
        }.flowOn(Dispatchers.IO)

        suspend fun getDataCategoryItem(privateKey: String): Flow<CategoryItemModel> = flow {
            val response = ApiService.api.fetchDataCategoryItem(privateKey)
            emit(response)
        }.flowOn(Dispatchers.IO)

        suspend fun getDataDetail(privateId: String): Flow<VideoDetailModel> = flow {
            val response = ApiService.api.fetchDataVideoNewsDetail(privateId)
            emit(response)
        }.flowOn(Dispatchers.IO)

        suspend fun getEPG(date: String): Flow<List<EPGModel>> = flow {
            val response = ApiService.api.fetchEPG(date)
            emit(response)
        }.flowOn(Dispatchers.IO)

        suspend fun getDataHighlight(privateKey: String): Flow<List<MediaModel>> = flow {
            val response = ApiService.api.fetchDataHighlight(privateKey)
            emit(response)
        }.flowOn(Dispatchers.IO)

        suspend fun getDataWeather(privateKey: String): Flow<List<WeatherModel>> = flow {
            val response = ApiService.api.fetchDataWeather(privateKey)
            emit(response)
        }.flowOn(Dispatchers.IO)

        //search
        suspend fun postDataSearch(searchBody: SearchBodyModel): Flow<List<SearchModel>> = flow {
            val response = ApiService.api.fetchDataSearch(searchBody)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}