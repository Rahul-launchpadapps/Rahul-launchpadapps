package com.app.okra.ui.add_meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.AddMealRepo
import com.app.okra.models.AddMealRequest
import com.app.okra.models.CommonData
import com.app.okra.models.FoodItemsRequest
import com.app.okra.models.MealUpdateRequest
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

    private var addMealLiveData = MutableLiveData<ApiData<Any>>()
    val _addMealLiveData: LiveData<ApiData<Any>>
        get() = addMealLiveData

    val addRequest = AddMealRequest()

    fun foodRecognition(imageUri: String?) {
        launchDataLoad {
            showProgressBar()
            multipart = getImagePart(File(imageUri), "file")
            val result = repo?.foodRecognition(multipart,"47c0f9ca2889db9294ee65c2fd4ccfaa")
            hideProgressBar()
            when (result) {
                is ApiResult.Success1<*> -> {
                    foodRecognitionLiveData.value = result.value.toString()
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

    fun prepareAddRequest(
        date: String?=null,
        image: String?=null,
        foodItems: ArrayList<FoodItemsRequest>?=null,
        foodType: String?=null,
        calories: CommonData?=null,
        carbs: CommonData?=null,
        fat: CommonData?=null,
        protein: CommonData?=null,
    ){
        if(!date.isNullOrEmpty()){
            addRequest.date = date
        }

        image?.let{
            addRequest.image = it
        }
        foodItems?.let{
            addRequest.foodItems = it
        }
        calories?.let{
            addRequest.calories = it
        }
        carbs?.let{
            addRequest.carbs = it
        }
        fat?.let{
            addRequest.fat = it
        }
        protein?.let{
            addRequest.protien = it
        }
        foodType?.let{
            addRequest.foodType = it
        }
    }

    fun addMeal() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.addMealLog(addRequest)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    addMealLiveData.value = result.value
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

}