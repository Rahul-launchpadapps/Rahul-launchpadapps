package com.app.okra.ui.add_meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.AddMealRepo
import com.app.okra.utils.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URLConnection

class AddMealViewModel(private val repo: AddMealRepo?) : BaseViewModel() {

    private var foodRecognitionLiveData = MutableLiveData<Any>()
    val _foodRecognitionLiveData: LiveData<Any>
        get() = foodRecognitionLiveData
    private lateinit var multipart: MultipartBody.Part

    fun foodRecognition(imageUri: String?) {
        launchDataLoad {
            showProgressBar()
            multipart = getImagePart(File(imageUri), "file")
            val result = repo?.foodRecognition(multipart,"47c0f9ca2889db9294ee65c2fd4ccfaa")
            hideProgressBar()
            when (result) {
                is ApiResult.Success1<*> -> {
                    foodRecognitionLiveData.value = result.value!!
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

    fun getImagePart(file: File, str: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            str,
            file.name,
            file.asRequestBody(URLConnection.guessContentTypeFromName(file.name).toMediaTypeOrNull())
        )
    }
}