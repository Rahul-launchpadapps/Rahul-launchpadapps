package com.app.okra.ui.logbook.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.MealLogsRepo
import com.app.okra.models.*
import com.app.okra.utils.*
import java.util.*
import kotlin.collections.ArrayList

class MealLogsViewModel(private val repo: MealLogsRepo?) : BaseViewModel() {


    private var mealLogLiveData = MutableLiveData<ApiData<MealListResponse>>()
    val _mealLogLiveData: LiveData<ApiData<MealListResponse>>
        get() = mealLogLiveData

   /* private var mealLogLiveData = MutableLiveData<ApiData<TestListResponse>>()
    val _mealLogLiveData: LiveData<ApiData<TestListResponse>>
        get() = mealLogLiveData
*/
    private var updateMealLiveData = MutableLiveData<ApiData<Any>>()
    val _updateMealLiveData: LiveData<ApiData<Any>>
        get() = updateMealLiveData

    private var deleteMealLiveData = MutableLiveData<ApiData<Any>>()
    val _deleteMealLiveData: LiveData<ApiData<Any>>
        get() = deleteMealLiveData

    var params= WeakHashMap<String, Any>()
    val updateRequest = MealUpdateRequest()

    fun prepareRequest(pageNo: Int,
                       fromDate: String?=null,
                       toDate: String?=null){
        params.clear()
        params[AppConstants.RequestParam.pageNo] = pageNo
        params[AppConstants.RequestParam.limit] = AppConstants.DATA_LIMIT

        if(!fromDate.isNullOrEmpty()){
            params[AppConstants.RequestParam.fromDate] = fromDate
        }
        if(!toDate.isNullOrEmpty()){
            params[AppConstants.RequestParam.toDate] = toDate
        }

    }

    fun prepareUpdateRequest(
        mealsId: String?=null,
        date: String?=null,
        image: String?=null,
        foodItems: ArrayList<FoodItemsRequest>?=null,
        foodType: String?=null,
        calories: CommonData?=null,
        carbs: CommonData?=null,
        fat: CommonData?=null,
        protein: CommonData?=null,
    ){
        updateRequest.mealsId= mealsId

        if(!date.isNullOrEmpty()){
            updateRequest.date = date
        }

        image?.let{
            updateRequest.image = it
        }
        foodItems?.let{
            updateRequest.foodItems = it
        }
        calories?.let{
            updateRequest.calories = it
        }
        carbs?.let{
            updateRequest.carbs = it
        }
        fat?.let{
            updateRequest.fat = it
        }
        protein?.let{
            updateRequest.protien = it
        }
        foodType?.let{
            updateRequest.foodType = it
        }
    }

    fun getMealLogs() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.getMealLogs(params)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    mealLogLiveData.value = result.value
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

    fun updateMeal() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.updateMealLog(updateRequest)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    updateMealLiveData.value = result.value
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
    fun deleteMeal(id :String) {
        launchDataLoad {
            showProgressBar()
            val result = repo?.deleteMeal(id)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    deleteMealLiveData.value = result.value
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