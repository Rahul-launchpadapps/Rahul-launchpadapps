package com.app.okra.data.network

import com.app.okra.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ApiService {

    @POST("v1/user/signup")
    suspend fun signUp(@Body request: InitialBoardingRequest): Response<BaseResponse<InitialBoardingResponse>>

    @POST("v1/user/login")
    suspend fun login(@Body request: InitialBoardingRequest): Response<BaseResponse<InitialBoardingResponse>>

    @POST("v1/user/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<BaseResponse<Any>>

    @POST("v1/user/verify-otp")
    suspend fun verifyOTP(@Body request: OTPVerifyRequest): Response<BaseResponse<InitialBoardingResponse>>

    @POST("v1/user/send-otp")
    suspend fun resendOTP(@Body request: ResendOtpRequest): Response<BaseResponse<Any>>

    @POST("v1/user/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<Any>>

    @POST("v1/user/change-password")
    suspend fun changePassword(@Body request: ResetPasswordRequest): Response<BaseResponse<Any>>

    @POST("v1/user/logout")
    suspend fun logout(): Response<BaseResponse<Any>>


    @GET("v1/user/profile")
    suspend fun getUserProfile(@Query("userId")userId: String): Response<BaseResponse<UserDetailResponse>>

    @PUT("v1/user/profile-update")
    @FormUrlEncoded
    suspend fun updateProfile(@FieldMap params: WeakHashMap<String, Any>): Response<BaseResponse<Any>>

    @PUT("v1/user/settings")
    suspend fun modifySettings(@Body  body: SettingRequest):
            Response<BaseResponse<Any>>

    @GET("v1/user/contactus")
    suspend fun contactUs(): Response<BaseResponse<ContactResponse>>


    @GET("v1/user/support-request-message")
    suspend fun getSupportRequestList(@QueryMap params: WeakHashMap<String, Any>):
            Response<BaseResponse<List<SupportResponse>>>


    @POST("v1/user/support-request")
    @FormUrlEncoded
    suspend fun sendSupportRequest(@FieldMap params: WeakHashMap<String, Any>):
            Response<BaseResponse<Any>>

    @GET("v1/test")
    suspend fun getTestLogs(@QueryMap params: WeakHashMap<String, Any>): Response<BaseResponse<TestListResponse>>

    @GET("v1/meals")
    suspend fun getMealLogs(@QueryMap params: WeakHashMap<String, Any>): Response<BaseResponse<MealListResponse>>

    @GET("v1/test")
    suspend fun getTestDetails(@Query("testId") testId: String): Response<BaseResponse<TestListResponse>>

    @PUT("v1/test")
    suspend fun updateTest(@Body params: TestUpdateRequest): Response<BaseResponse<Any>>

   @DELETE("v1/test")
    suspend fun deleteTest(@Query("testId")  id:String): Response<BaseResponse<Any>>

    @DELETE("v1/meals")
    suspend fun deleteMeal(@Query("mealsId")  id:String): Response<BaseResponse<Any>>

    @PUT("v1/meals")
    suspend fun updateMeal(@Body params: MealUpdateRequest): Response<BaseResponse<Any>>

    @Multipart
    @POST("v1/foodrecognition/full")
    suspend fun foodRecognition(@Part file: MultipartBody.Part? = null, @Query("user_key") key:String):
            Response<Any>
}