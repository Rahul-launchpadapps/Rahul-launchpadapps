package com.app.okra.data.repo

import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.models.AddMealRequest
import okhttp3.MultipartBody

interface AddMealRepo {
    suspend fun foodRecognition(multipart: MultipartBody.Part?,
                                key: String): Any
    suspend fun addMealLog(params: AddMealRequest): ApiResult<ApiData<Any>>
}