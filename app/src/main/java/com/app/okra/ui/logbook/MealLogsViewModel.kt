package com.app.okra.ui.logbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.MealLogsRepo
import com.app.okra.data.repo.TestLogsRepo
import com.app.okra.models.MealListResponse
import com.app.okra.models.TestListResponse
import com.app.okra.models.UserDetailResponse
import com.app.okra.utils.*
import java.util.*

class MealLogsViewModel(private val repo: MealLogsRepo?) : BaseViewModel() {


   /* private var mealLogLiveData = MutableLiveData<ApiData<MealListResponse>>()
    val _mealLogLiveData: LiveData<ApiData<MealListResponse>>
        get() = mealLogLiveData*/

    private var mealLogLiveData = MutableLiveData<ApiData<TestListResponse>>()
    val _mealLogLiveData: LiveData<ApiData<TestListResponse>>
        get() = mealLogLiveData

    var params= WeakHashMap<String, Any>()

    fun  setRequest(
        pageNo: Int?=null,
        limit: String?=null,
    ){

        params = WeakHashMap<String, Any>()

        pageNo?.let{
            params[AppConstants.RequestParam.pageNo] =pageNo
        }

         params[AppConstants.RequestParam.limit] =AppConstants.DATA_LIMIT

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
}