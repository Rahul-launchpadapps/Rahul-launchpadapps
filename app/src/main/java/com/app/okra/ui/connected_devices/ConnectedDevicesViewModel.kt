package com.app.okra.ui.connected_devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.ConnectedDevicesRepo
import com.app.okra.data.repo.SettingRepoImpl
import com.app.okra.models.ContactResponse
import com.app.okra.models.SettingRequest
import com.app.okra.utils.Event

class ConnectedDevicesViewModel(private val repo: ConnectedDevicesRepo?) : BaseViewModel() {

    companion object {

    }

    private var logoutLiveData = MutableLiveData<ApiData<Any>>()
    val _logoutLiveData: LiveData<ApiData<Any>> get() = logoutLiveData

    private var contactUsLiveData = MutableLiveData<ApiData<ContactResponse>>()
    val _contactUsLiveData: LiveData<ApiData<ContactResponse>> get() = contactUsLiveData

    private var connectedDevicesLiveData = MutableLiveData<ApiData<Any>>()
    val _connectedDevicesLiveData: LiveData<ApiData<Any>>
        get() = connectedDevicesLiveData

    var settingRequest= SettingRequest()

    fun  setSettingRequest(
            inAppStatus: Boolean?=null,
            pushNotification: Boolean?=null,
            bloodGlucoseUnit: String?=null,
            hyperBloodGlucoseValue: Int?=null,
            hypoBloodGlucoseValue: Int?=null
    ){

        inAppStatus?.let {
            settingRequest.inappNotificationStatus = it
        }
        pushNotification?.let {
            settingRequest.pushNotificationStatus = it
        }
        bloodGlucoseUnit?.let {
            settingRequest.bloodGlucoseUnit = it
        }
        hyperBloodGlucoseValue?.let {
            settingRequest.hyperBloodGlucoseValue = it
        }
        hypoBloodGlucoseValue?.let {
            settingRequest.hypoBloodGlucoseValue = it
        }
    }

    //  Upload
/*
    fun uploadUserPic() {
        showProgressBar()
        launchDataLoad {
            val result = repo?.uploadUserPic(params)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    uploadImageLiveData.value = result.value
                }
                is ApiResult.GenericError -> {
                    errorObserver.value = Event(result.message)
                }
                is ApiResult.NetworkError -> {
                    errorObserver.value = Event("Network Issue")
                }
            }
        }
    }
*/


    fun getPreviousDevices() {
        launchDataLoad {
             showProgressBar()
                val result = repo?.getPreviouslyConnectedDeviceList()
                hideProgressBar()
                when (result) {
                    is ApiResult.Success -> {
                        connectedDevicesLiveData.value = result.value
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