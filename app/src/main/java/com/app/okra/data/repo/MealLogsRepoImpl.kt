package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.network.ApiService
import com.app.okra.data.network.BaseRepo
import com.app.okra.models.MealListResponse
import com.app.okra.models.TestListResponse
import com.app.okra.models.UserDetailResponse
import kotlinx.coroutines.Dispatchers
import java.util.*

class MealLogsRepoImpl constructor(
        private val apiService: ApiService,
) : BaseRepo(apiService),
    MealLogsRepo {

    override suspend fun getMealLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<TestListResponse>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.getTestLogs(params)
        }
    }
    /*override suspend fun getMealLogs(params: WeakHashMap<String, Any>): ApiResult<ApiData<MealListResponse>> {
        return safeApiCall(Dispatchers.IO) {
            apiService.getMealLogs(params)
        }
    }*/

}


