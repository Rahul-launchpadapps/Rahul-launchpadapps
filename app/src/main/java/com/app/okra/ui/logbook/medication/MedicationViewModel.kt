package com.app.okra.ui.logbook.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.repo.MedicationRepo
import com.app.okra.models.*
import com.app.okra.utils.*
import java.util.*
import kotlin.collections.ArrayList

class MedicationViewModel(private val repo: MedicationRepo?) : BaseViewModel() {

    private var medicationLiveData = MutableLiveData<ApiData<MedicationResponse>>()
    val _medicationLiveData: LiveData<ApiData<MedicationResponse>>
        get() = medicationLiveData

    private var updateMedicationLiveData = MutableLiveData<ApiData<Any>>()
    val _updateMedicationLiveData: LiveData<ApiData<Any>>
        get() = updateMedicationLiveData

    private var addMedicationLiveData = MutableLiveData<ApiData<Any>>()
    val _addMedicationLiveData: LiveData<ApiData<Any>>
        get() = addMedicationLiveData

    private var deleteMedicationLiveData = MutableLiveData<ApiData<Any>>()
    val _deleteMedicationLiveData: LiveData<ApiData<Any>>
        get() = deleteMedicationLiveData

    private var searchMedicationLiveData = MutableLiveData<ApiData<Any>>()
    val _searchMedicationLiveData: LiveData<ApiData<Any>>
        get() = searchMedicationLiveData

    var params = WeakHashMap<String, Any>()
    val updateRequest = MealUpdateRequest()
    val addRequest = AddMedicationRequest()

    fun prepareRequest(
        pageNo: Int,
        fromDate: String? = null,
        toDate: String? = null,
        type: String? = null
    ) {
        params.clear()
        params[AppConstants.RequestParam.pageNo] = pageNo
        params[AppConstants.RequestParam.limit] = AppConstants.DATA_LIMIT

        if (!fromDate.isNullOrEmpty()) {
            params[AppConstants.RequestParam.from] = fromDate
        }
        if (!toDate.isNullOrEmpty()) {
            params[AppConstants.RequestParam.to] = toDate
        }
        if (!type.isNullOrEmpty()) {
            params[AppConstants.RequestParam.type] = type
        }
    }

    fun prepareUpdateRequest(
        mealsId: String? = null,
        date: String? = null,
        image: String? = null,
        foodItems: ArrayList<FoodItemsRequest>? = null,
        foodType: String? = null,
        calories: CommonData? = null,
        carbs: CommonData? = null,
        fat: CommonData? = null,
        protein: CommonData? = null,
    ) {
        updateRequest.mealsId = mealsId

        if (!date.isNullOrEmpty()) {
            updateRequest.date = date
        }

        image?.let {
            updateRequest.image = it
        }
        foodItems?.let {
            updateRequest.foodItems = it
        }
        calories?.let {
            updateRequest.calories = it
        }
        carbs?.let {
            updateRequest.carbs = it
        }
        fat?.let {
            updateRequest.fat = it
        }
        protein?.let {
            updateRequest.protien = it
        }
        foodType?.let {
            updateRequest.foodType = it
        }
    }

    fun getMedicationList() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.getMedicationList(params)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    medicationLiveData.value = result.value
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

    fun addMedication(name: String, unit: String, quant: Int) {
        launchDataLoad {
            showProgressBar()
            addRequest.medicineName = name
            addRequest.unit = unit
            addRequest.quantity = quant
            val result = repo?.addMedication(addRequest)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    addMedicationLiveData.value = result.value
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

    fun updateMedication() {
        launchDataLoad {
            showProgressBar()
            val result = repo?.updateMedication(updateRequest)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    updateMedicationLiveData.value = result.value
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

    fun deleteMedication(id: String) {
        launchDataLoad {
            showProgressBar()
            val result = repo?.deleteMedication(id)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    deleteMedicationLiveData.value = result.value
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

    fun searchMedication(search: String) {
        launchDataLoad {
            showProgressBar()
            val result = repo?.searchMedication(search)
            hideProgressBar()
            when (result) {
                is ApiResult.Success -> {
                    searchMedicationLiveData.value = result.value
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