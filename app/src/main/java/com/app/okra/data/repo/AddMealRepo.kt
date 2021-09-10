package com.app.okra.data.repo

import okhttp3.MultipartBody

interface AddMealRepo {
    suspend fun foodRecognition(multipart: MultipartBody.Part?,
                                key: String): Any
}