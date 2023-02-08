package com.cnpm.cnpm_vnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpm.vnews.model.MenuModel
import com.cnpm.vnews.model.VideoDetailModel
import com.cnpm.vnews.respository.APIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    val mostMenuModel = MutableLiveData<MenuModel>()
    fun mostViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "newsMostViewModel: ${e.message}")
                }.collect { response ->
                    mostMenuModel.value = response
                }
        }
    }

    val highlightMenuModel = MutableLiveData<MenuModel>()
    fun highlightViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "highlightViewModel: ${e.message}")
                }.collect { response ->
                    highlightMenuModel.value = response
                }
        }
    }

    val newestMenuModel = MutableLiveData<MenuModel>()
    fun newestViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "newestViewModel: ${e.message}")
                }.collect { response ->
                    newestMenuModel.value = response
                }
        }
    }

    val videoDetailModel = MutableLiveData<VideoDetailModel>()
    fun getNewsMediaWithCheck(privateId: String): LiveData<Boolean> {
        val haveResult = MutableLiveData<Boolean>()
        viewModelScope.launch {
            APIRepository.getDataDetail(privateId)
                .catch { e ->
                    Log.e("api", "newestViewModel: ${e.message}")
                    haveResult.value = false
                }.collect { response ->
                    videoDetailModel.value = response
                    haveResult.value = true
                }
        }
        return haveResult
    }

    fun getListMediaRelatedDetail(listId: List<String>): LiveData<List<VideoDetailModel>> {
        val resListMediaDetail = MutableLiveData<List<VideoDetailModel>>()
        val listMediaDetail = mutableListOf<VideoDetailModel>()
        listId.forEach { id ->
            viewModelScope.launch {
//                viewModelScope.launch {
                    APIRepository.getDataDetail(id)
                        .catch { e ->
                            Log.e("api", "newestViewModel: ${e.message}")
                        }.collect { response ->
                            listMediaDetail.add(response)
                        }
//                }
                resListMediaDetail.value = listMediaDetail
            }
        }
        return resListMediaDetail
    }
}