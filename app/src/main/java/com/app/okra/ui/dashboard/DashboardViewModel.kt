package com.app.okra.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.DashboardRepo
import com.app.okra.utils.Event

class DashboardViewModel(private val repo: DashboardRepo?) : BaseViewModel() {


    private var logoutLiveData = MutableLiveData<ApiData<Any>>()
    val _logoutLiveData: LiveData<ApiData<Any>> get() = logoutLiveData

    fun onLogout() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.onLogout()
            hideProgressBar()
            when (result) {
                is ApiResult.Success<Any> ->{
                    logoutLiveData.value = result.value
                }
                is ApiResult.GenericError -> {
                    errorObserver.value = Event(ApiData(message =result.message))
                }
                is ApiResult.NetworkError -> {
                    errorObserver.value = Event(ApiData(message = "Network Issue"))
                }
            }
        }
    }

}