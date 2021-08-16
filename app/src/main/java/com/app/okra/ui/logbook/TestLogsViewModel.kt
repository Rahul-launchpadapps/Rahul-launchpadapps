package com.app.okra.ui.logbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.TestLogsRepo
import com.app.okra.models.UserDetailResponse
import com.app.okra.utils.*
import java.util.*

class TestLogsViewModel(private val repo: TestLogsRepo?) : BaseViewModel() {

    private var profileInfoLiveData = MutableLiveData<ApiData<UserDetailResponse>>()
    val _profileInfoLiveData: LiveData<ApiData<UserDetailResponse>>
        get() = profileInfoLiveData

    var params= WeakHashMap<String, Any>()

    fun getTestLogs() {
        launchDataLoad {
            params[AppConstants.RequestParam.pageNo] = "1"
            params[AppConstants.RequestParam.limit] ="10"

            showProgressBar()
            val result = repo?.getTestLogs(params)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    profileInfoLiveData.value = result.value
                }
                is ApiResult.GenericError -> {
                    errorObserver.value = Event(ApiData(message = result.message))
                }
                is ApiResult.NetworkError -> {
                    errorObserver.value = Event(ApiData(message = "Network Issue"))
                }
            }
        }
    }
}