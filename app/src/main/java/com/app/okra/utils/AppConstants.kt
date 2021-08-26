package com.app.okra.utils

import com.app.okra.BuildConfig

class AppConstants {

    class Pref_Key {
        companion object {

            const val USER_TYPE = "userType"
            const val ACCESS_TOKEN = "accessToken"
            const val USER_ID = "userId"
            const val EMAIL_ID= "email"
            const val IS_LOGGED_IN = "isLoggedIn"
            const val IS_FIRST_TIME = "isFirstTime"
            const val PASSWORD = "password"
            const val AGE = "age"
            const val MOBILE = "mobile"
            const val PUSH_NOTIFICATION = "pushNotification"
            const val IN_APP_NOTIFICATION = "inAppNotification"
            const val BLOOD_GLUCOSE_UNIT = "bloodGlucoseUnit"
            const val HYPER_BLOOD_GLUCOSE_UNIT = "hyperBloodGlucoseValue"
            const val HYPO_BLOOD_GLUCOSE_UNIT = "hypoBloodGlucoseValue"

            const val USER_DATA="user_data"
            const val NAME = "Name"
            const val PROFILE_PIC = "profilePic"
            const val DEVICE_TOKEN = "device_token"
        }
    }


    object PermissionCodes {
        const val PERMISSION_STORAGE: Int = 101
        const val PERMISSION_CAMERA: Int = 102
        const val PERMISSION_LOCATION: Int = 103

    }

    object RequestCodes {
        const val REQUEST_PICK_IMAGE_FROM_GALLERY: Int = 101
        const val REQUEST_CLICK_IMAGE_FROM_CAMERA: Int = 102
    }

    class RequestParam {
        companion object {
            const val profilePic = "profilePicture"
            const val name: String = "name"
            const val mobileNo: String = "mobileNo"
            const val age: String = "age"
            const val pageNo: String = "pageNo"
            const val limit: String = "limit"
            const val title: String = "title"
            const val description: String = "description"
        }
    }


    class Intent_Constant {
        companion object {

            const val FROM_SCREEN = "fromScreen"
            const val EMAIL: String = "EMAIL"

            const val TYPE ="type"
        }

    }


    class NotificationConstants {
        companion object {

        }
    }


    companion object {
        const val DATA_LIMIT= 10
        const val SCREEN_TYPE = "screen_type"
        const val EMAIL = "email"
        const val android = "Android"
        const val SHOW_TAG = "1"
        const val HIDE_TAG = "2"
        const val LOGIN = "login"
        const val ALLOWED_FILE_SIZE =10
        const val ISO_FORMATE= "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        const val ISO_FORMAT= "yyyy-MM-dd'T'HH:mm:ss"
    }

    object ContentManagementUrl {
        const val PRIVACY_POLICY_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "PRIVACY_POLICY"
        const val TERM_AND_COND_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "TERMS_OF_SERVICE_AGREEMENT"
        const val ABOUT_US_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "CODE_OF_CONDUCT"
        const val FAQ_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "DWES_CHECK_SIGNED"
    }

    class Testing_Time {
        companion object {
            const val AFTER_MEAL = "AFTER_MEAL"
            const val BEFORE_MEAL = "BEFORE_MEAL"
        }

    }

}