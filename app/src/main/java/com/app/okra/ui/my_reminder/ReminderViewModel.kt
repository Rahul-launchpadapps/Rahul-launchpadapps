package com.app.okra.ui.my_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.ReminderRepo
import com.app.okra.models.UserDetailResponse
import com.app.okra.utils.*

class ReminderViewModel(private val repo: ReminderRepo?) : BaseViewModel() {

    private var setReminderLiveData = MutableLiveData<ApiData<Any>>()
    val _setReminderLiveData: LiveData<ApiData<Any>>
        get() = setReminderLiveData

    private var profileInfoLiveData = MutableLiveData<ApiData<UserDetailResponse>>()
    val _profileInfoLiveData: LiveData<ApiData<UserDetailResponse>>
        get() = profileInfoLiveData

    fun setReminder(data : HashMap<String,Any>) {
        launchDataLoad {
             showProgressBar()
                val result = repo?.setReminder(data)
                hideProgressBar()
                when (result) {
                    is ApiResult.Success -> {
                        setReminderLiveData.value = result.value
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

    fun getProfileInfo(userId:String) {
        launchDataLoad {
            showProgressBar()
            val result = repo?.apiForProfileInfo(userId)
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