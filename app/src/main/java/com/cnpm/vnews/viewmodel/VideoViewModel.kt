package com.cnpm.vnews.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpm.vnews.model.CategoryItemModel
import com.cnpm.vnews.model.MenuModel
import com.cnpm.vnews.respository.APIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class VideoViewModel : ViewModel() {

    fun menuVideoViewModel(privateKey: String): MutableLiveData<MenuModel> {
        val responseLiveData: MutableLiveData<MenuModel> = MutableLiveData()
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "menuVideoViewModel: ${e.message}")
                }.collect { response ->
                    responseLiveData.value = response
//                    menuVideoModel.value = response
                }
        }
        return responseLiveData
    }

    fun categoryItemViewModel(privateKey: String): MutableLiveData<CategoryItemModel> {
        val responseLiveData: MutableLiveData<CategoryItemModel> = MutableLiveData()
        viewModelScope.launch {
            APIRepository.getDataCategoryItem(privateKey)
                .catch { e ->
                    Log.e("api", "menuCategoryViewModel: ${e.message}")
                }.collect { response ->
                    responseLiveData.value = response
                }
        }
        return responseLiveData
    }


}