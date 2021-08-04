package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.network.ApiService
import com.app.okra.data.network.BaseRepo
import com.app.okra.models.ContactResponse
import com.app.okra.models.SettingRequest
import kotlinx.coroutines.Dispatchers

class ConnectedDevicesRepoImpl constructor(
    private val apiService: ApiService,
) : BaseRepo(apiService),
    ConnectedDevicesRepo {
    override suspend fun updateNotificationStatus(data: SettingRequest): ApiResult<ApiData<Any>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.modifySettings(data)
        }
    }

    override suspend fun contactUs(): ApiResult<ApiData<ContactResponse>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.contactUs()
        }
    }
}