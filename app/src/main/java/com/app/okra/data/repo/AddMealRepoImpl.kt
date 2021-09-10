package com.app.okra.data.repo

import com.app.okra.data.network.ApiService
import com.app.okra.data.network.BaseRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody

class AddMealRepoImpl constructor(
    private val apiService: ApiService,
) : BaseRepo(apiService),
    AddMealRepo {

    override suspend fun foodRecognition(
        multipart: MultipartBody.Part?,
        key: String
    ): Any {
        return safeApiCallWithoutBaseResponse(Dispatchers.IO) {
            apiService.foodRecognition(multipart, key)
        }
    }
}


