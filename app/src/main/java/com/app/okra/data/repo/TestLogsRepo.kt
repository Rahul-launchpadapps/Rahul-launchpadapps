package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.models.UserDetailResponse
import retrofit2.http.QueryMap
import java.util.*

interface TestLogsRepo {
    suspend fun getTestLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<UserDetailResponse>>
}