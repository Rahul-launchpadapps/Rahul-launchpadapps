package com.app.okra.ui.boarding.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.data.network.ApiResult
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.data.repo.InitialBoardingRepo
import com.app.okra.extension.isEmailValid
import com.app.okra.extension.isPasswordValid
import com.app.okra.models.InitialBoardingRequest
import com.app.okra.models.InitialBoardingResponse
import com.app.okra.utils.AppConstants
import com.app.okra.utils.Event
import com.app.okra.utils.MessageConstants
import com.app.okra.utils.ToastData

class InitialBoardingViewModel(private val repo : InitialBoardingRepo?) : BaseViewModel() {

    private var loginLiveData = MutableLiveData<ApiData<InitialBoardingResponse>>()
    val _loginLiveData: LiveData<ApiData<InitialBoardingResponse>>
        get() = loginLiveData

    companion object {
        const val FIELD_EMAIL = "email"
        const val FIELD_PASS = "pass"
        const val OTHER = "other"
    }

    private var deviceToken :String? = null
    init {
         deviceToken  = PreferenceManager.getString(AppConstants.Pref_Key.DEVICE_TOKEN)
    }

    private val initBoardingRequest  = InitialBoardingRequest()
    fun setLoginValue(email: String, password:String){
        initBoardingRequest.email =email
        initBoardingRequest.password =password.trim()
        initBoardingRequest.deviceId = AppConstants.android
        initBoardingRequest.deviceToken =deviceToken
    }

    fun login(){
        if(validateData()){
            showProgressBar()

            launchDataLoad {
                val result = repo?.onLogin(initBoardingRequest)
                hideProgressBar()
                when (result) {
                    is ApiResult.Success -> {
                        loginLiveData.value = result.value
                    }
                    is ApiResult.GenericError -> {
                        errorObserver.value = Event(
                            ApiData(null, result.errorCode, result.message,result.type,result.message))
                    }
                    else -> {
                        errorObserver.value = Event(ApiData(message = "Network Issue"))
                    }
                }
            }
        }
    }
    fun signUp( isChecked :Boolean){
        if(validateData(isChecked)){
            launchDataLoad {
                showProgressBar()
                val result = repo?.onLogin(initBoardingRequest)
                hideProgressBar()
                when (result) {
                    is ApiResult.Success<InitialBoardingResponse> -> {
                        loginLiveData.value = result.value
                    }
                    is ApiResult.GenericError -> {
                        errorObserver.value = Event(ApiData(message =result.message))
                    }
                    is ApiResult.NetworkError -> {
                        errorObserver.value = Event(ApiData(message ="Network Issue"))
                    }
                }
            }
        }
    }

    private fun validateData(isChecked :Boolean): Boolean {
        return when{
            initBoardingRequest.email.isNullOrBlank() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.enter_email))
                false
            }
            !initBoardingRequest.email!!.isEmailValid() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.invalid_email))
                false
            }
            initBoardingRequest.password.isNullOrBlank() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.enter_pass))
                false
            }
            !initBoardingRequest.password!!.isPasswordValid() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.invalid_pass_message))
                false
            }
            !isChecked -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.please_agree))
                false
            }
            else -> true
        }
    }

    private fun validateData(): Boolean {
        return when{
            initBoardingRequest.email.isNullOrBlank() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.enter_email,FIELD_EMAIL))
                false
            }
            !initBoardingRequest.email!!.isEmailValid() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.invalid_email, FIELD_EMAIL))
                false
            }
            initBoardingRequest.password.isNullOrBlank() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.enter_pass,FIELD_PASS))
                false
            }
            !initBoardingRequest.password!!.isPasswordValid() -> {
                toastObserver.value =Event(ToastData(MessageConstants.Errors.invalid_pass_message,FIELD_PASS))
                false
            }
            else -> true
        }
    }
}