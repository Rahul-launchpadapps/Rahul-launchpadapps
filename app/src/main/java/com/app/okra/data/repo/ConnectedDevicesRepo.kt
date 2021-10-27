package com.app.okra.data.repo

import com.app.okra.data.network.ApiResult
import com.app.okra.data.network.ApiData
import com.app.okra.models.ContactResponse
import com.app.okra.models.InitialBoardingRequest
import com.app.okra.models.InitialBoardingResponse
import com.app.okra.models.SettingRequest


interface ConnectedDevicesRepo {

    suspend fun getPreviouslyConnectedDeviceList()
            : ApiResult<ApiData<Any>>

}