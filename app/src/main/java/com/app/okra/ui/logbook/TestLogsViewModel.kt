package com.app.okra.ui.logbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.TestLogsRepo
import com.app.okra.models.TestListResponse
import com.app.okra.models.TestUpdateRequest
import com.app.okra.utils.*
import com.app.okra.utils.AppConstants.Companion.DATA_LIMIT
import java.util.*
import kotlin.collections.ArrayList

class TestLogsViewModel(private val repo: TestLogsRepo?) : BaseViewModel() {

    private var testListLiveData = MutableLiveData<ApiData<TestListResponse>>()
    val _testListLiveData: LiveData<ApiData<TestListResponse>>
        get() = testListLiveData

    private var updateTestLiveData = MutableLiveData<ApiData<Any>>()
    val _updateTestLiveData: LiveData<ApiData<Any>>
        get() = updateTestLiveData

    private var deleteTestLiveData = MutableLiveData<ApiData<Any>>()
    val _deleteTestLiveData: LiveData<ApiData<Any>>
        get() = deleteTestLiveData

    private var testDetailLiveData = MutableLiveData<ApiData<TestListResponse>>()
    val _testDetailLiveData: LiveData<ApiData<TestListResponse>>
        get() = testDetailLiveData

    var params= WeakHashMap<String, Any>()
    val updateRequest = TestUpdateRequest()



  fun prepareRequest(pageNo: Int,
                       testingTime: String?=null,
                       fromDate: String?=null,
                       toDate: String?=null){
        params.clear()
        params[AppConstants.RequestParam.pageNo] = pageNo
        params[AppConstants.RequestParam.limit] = DATA_LIMIT

        if(!testingTime.isNullOrEmpty()){
            params[AppConstants.RequestParam.testingTime] = testingTime
        }

        if(!fromDate.isNullOrEmpty()){
            params[AppConstants.RequestParam.fromDate] = fromDate
        }
        if(!toDate.isNullOrEmpty()){
            params[AppConstants.RequestParam.toDate] = toDate
        }

    }

    fun prepareUpdateRequest(
                        testId: String?=null,
                       testingTime: String?=null,
                        bloodGlucose: Int?=null,
                        bloodPressure: Int?=null,
                        insulin: Int?=null,
                        additionalNotes: String?=null,
                        mealsBefore: MutableList<String>?=null,
                        mealsAfter: MutableList<String>?=null,
    ){


        updateRequest.testId= testId

        if(!testingTime.isNullOrEmpty()){
            updateRequest.testingTime = testingTime
        }

        bloodGlucose?.let{
            updateRequest.bloodGlucose = it
        }
        bloodPressure?.let{
           updateRequest.bloodPressure = it
        }
        insulin?.let{
           updateRequest.insulin = it
        }
        if(!additionalNotes.isNullOrEmpty()){
           updateRequest.additionalNotes = additionalNotes
        }

       updateRequest.mealsBefore = mealsBefore?.toCollection(ArrayList())
       updateRequest.mealsAfter = mealsAfter?.toCollection(ArrayList())

       /* if(!mealsBefore.isNullOrEmpty()){
            params[AppConstants.RequestParam.mealsBefore] = mealsBefore
        }
        if(!mealsAfter.isNullOrEmpty()){
            params[AppConstants.RequestParam.mealsAfter] = mealsAfter
        }*/

    }

    fun getTestLogs() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.getTestLogs(params)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    testListLiveData.value = result.value
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

fun updateTest() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.updateTestLog(updateRequest)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    updateTestLiveData.value = result.value
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
fun deleteTest(id :String) {
        launchDataLoad {
            showProgressBar()
            val result = repo?.deleteTest(id)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    deleteTestLiveData.value = result.value
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

    fun getTestDetails(testId: String) {
        launchDataLoad {

            showProgressBar()
            val result = repo?.getTestDetails(testId)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    testDetailLiveData.value = result.value
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