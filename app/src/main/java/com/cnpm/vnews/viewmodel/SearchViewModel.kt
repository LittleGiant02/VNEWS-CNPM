package com.cnpm.vnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpm.vnews.model.SearchBodyModel
import com.cnpm.vnews.model.SearchModel
import com.cnpm.vnews.respository.APIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
//    fun getSearchByUrl(url: String,loadMorBody: SearchBody) : LiveData<List<MediaVnews>>{
//        val listLoadMore = MutableLiveData<List<MediaVnews>>()
//        viewModelScope.launch {
//            when (val res = rootRepo.getSearch(url,loadMorBody)) {
//                is Resource.Success -> {
//                    listLoadMore .value = res.data
//                }
//                is Resource.Error -> {
//                }
//            }
//        }
//        return listLoadMore
//    }

    fun postSearch(searchBody: SearchBodyModel): LiveData<List<SearchModel>> {
        val listSearchSuggest = MutableLiveData<List<SearchModel>>()
        viewModelScope.launch {
            APIRepository.postDataSearch(searchBody)
                .catch { e ->
                    Log.e("api", "newsMostViewModel: ${e.message}")
                }.collect { response ->
                    listSearchSuggest.value = response
                }
        }
        return listSearchSuggest
    }


}