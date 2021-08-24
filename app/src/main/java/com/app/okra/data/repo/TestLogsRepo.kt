package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.models.TestListResponse
import java.util.*

interface TestLogsRepo {
    suspend fun getTestLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<TestListResponse>>
    suspend fun getTestDetails(testId: String): ApiResult<ApiData<TestListResponse>>
}