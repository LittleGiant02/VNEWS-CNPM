package com.cnpm.cnpm_vnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.model.MenuModel
import com.cnpm.vnews.model.WeatherModel
import com.cnpm.vnews.respository.APIRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val highlightMenuModel = MutableLiveData<List<MediaModel>>()
    fun highlightViewModel(privateKey: String): LiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        viewModelScope.launch {
            APIRepository.getDataHighlight(privateKey)
                .catch { e ->
                    Log.e("api", "newsMostViewModel: ${e.message}")
                    isSuccess.value = false
                }.collect { response ->
                    highlightMenuModel.value = response
                    isSuccess.value = true
                }
        }
        return isSuccess
    }

    val newestMenuModel = MutableLiveData<MenuModel>()
    fun newestViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "NewestViewModel: ${e.message}")
                }.collect { response ->
                    newestMenuModel.value = response
                }
        }
    }

    val infographicMenuModel = MutableLiveData<MenuModel>()
    fun infographicMenuModelViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataMenu(privateKey)
                .catch { e ->
                    Log.e("api", "infoViewModel: ${e.message}")
                }.collect { response ->
                    infographicMenuModel.value = response
                }
        }
    }

    val weatherModel = MutableLiveData<List<WeatherModel>>()
    fun weatherModelViewModel(privateKey: String) {
        viewModelScope.launch {
            APIRepository.getDataWeather(privateKey)
                .catch { e ->
                    Log.e("api", "weatherViewModel: ${e.message}")
                }.collect { response ->
                    weatherModel.value = response
                }
        }
    }
}