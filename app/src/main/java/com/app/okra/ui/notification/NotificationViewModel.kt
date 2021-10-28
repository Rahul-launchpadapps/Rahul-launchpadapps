package com.app.okra.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.BloodGlucoseRepo
import com.app.okra.data.repo.NotificationRepo
import com.app.okra.models.InsightResponse
import com.app.okra.models.NotificationResponse
import com.app.okra.utils.*
import java.util.*

class NotificationViewModel(private val repo: NotificationRepo?) : BaseViewModel() {

    private var notificationLiveData = MutableLiveData<ApiData<NotificationResponse>>()
    val _notificationLiveData: LiveData<ApiData<NotificationResponse>>
        get() = notificationLiveData

    fun getNotification(page:Int) {
        launchDataLoad {

            showProgressBar()
            val result = repo?.getNotification(page,AppConstants.DATA_LIMIT)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    notificationLiveData.value = result.value
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