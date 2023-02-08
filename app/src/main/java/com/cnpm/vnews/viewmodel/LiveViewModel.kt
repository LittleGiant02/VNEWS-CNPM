package com.cnpm.cnpm_vnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpm.vnews.model.EPGModel
import com.cnpm.vnews.respository.APIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LiveViewModel : ViewModel() {
    val listEPGViewModel = MutableLiveData<List<EPGModel>>()
    fun epgViewModel(date: String): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        viewModelScope.launch {
            APIRepository.getEPG(date)
                .catch { e ->
                    isSuccess.value = false
                    Log.e("api", "menuVideoViewModel: ${e.message}")
                }.collect { response ->
                    listEPGViewModel.value = response
                    isSuccess.value = true
                }
        }
        return isSuccess
    }
}