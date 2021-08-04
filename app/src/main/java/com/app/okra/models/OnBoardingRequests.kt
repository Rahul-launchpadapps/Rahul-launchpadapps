package com.app.okra.models

class ResetPasswordRequest(
        var email: String? =null,
        var newPassword: String? =null,
        var confirmPassword: String? =null,
        var oldPassword: String? =null,
        var password: String? =null
)

class InitialBoardingRequest(
        var email: String? =null,
        var password: String? =null,
        var userType: String?=null,
        var deviceId: String? =null,
        var deviceToken: String? =null
)

class ForgotPasswordRequest(
        var email: String? =null
)

class OTPVerifyRequest(
        var email: String? =null,
        var type: String? =null,
        var mobileNo: String? =null,
        var otp: String? =null,
        var deviceId: String? =null,
        var deviceToken: String? =null,
)

class ResendOtpRequest {
        var email: String? =null
        var type: String? =null
        var mobileNo: String? =null
}
