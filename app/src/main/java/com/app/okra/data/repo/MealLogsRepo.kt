package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.models.MealListResponse
import com.app.okra.models.TestListResponse
import java.util.*

interface MealLogsRepo {
    //suspend fun getMealLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<MealListResponse>>
    suspend fun getMealLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<TestListResponse>>
}