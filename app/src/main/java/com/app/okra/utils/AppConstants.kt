package com.app.okra.utils

import com.app.okra.BuildConfig

class AppConstants {

    class Pref_Key {
        companion object {

            const val USER_TYPE = "userType"
            const val ACCESS_TOKEN = "accessToken"
            const val IS_APPROVED = "isApproved"
            const val IS_VERIFIED = "isVerified"
            const val USER_ID = "userId"
            const val EMAIL_ID = "email"
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

            const val USER_DATA = "user_data"
            const val NAME = "Name"
            const val PROFILE_PIC = "profilePic"
            const val DEVICE_TOKEN = "device_token"
            const val RECENT_MEDICINE = "recent_medicine"
        }
    }


    object PermissionCodes {
        const val PERMISSION_STORAGE: Int = 101
        const val PERMISSION_CAMERA: Int = 102
        const val PERMISSION_LOCATION: Int = 103

    }

    object RequestOrResultCodes {
        const val REQUEST_PICK_IMAGE_FROM_GALLERY: Int = 101
        const val REQUEST_CLICK_IMAGE_FROM_CAMERA: Int = 102
        const val RESULT_CODE_TEST_LOG_UPDATED: Int = 103
        const val RESULT_CODE_TEST_LOG_DELETED: Int = 104
        const val RESULT_CODE_MEAL_LOG_UPDATED: Int = 103
        const val RESULT_CODE_MEAL_LOG_DELETED: Int = 106
        const val MEAL_ADDED: Int = 107
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
            const val testingTime: String = "testingTime"
            const val fromDate: String = "fromDate"
            const val toDate: String = "toDate"
            const val testId: String = "testId"
            const val bloodGlucose: String = "bloodGlucose"
            const val bloodPressure: String = "bloodPressure"
            const val insulin: String = "insulin"
            const val additionalNotes: String = "additionalNotes"
            const val mealsBefore: String = "mealsBefore"
            const val mealsAfter: String = "mealsAfter"
            const val user_key: String = "user_key"
            const val type: String = "type"
            const val timesOfConsideration: String = "timesOfConsideration"
            const val reminderType: String = "reminderType"
            const val startDate: String = "startDate"
            const val time: String = "time"
            const val repeatType: String = "repeatType"
            const val endRepeatType: String = "endRepeatType"
            const val endDate: String = "endDate"
            const val from: String = "from"
            const val to: String = "to"
        }
    }


    class Intent_Constant {
        companion object {

            const val FROM_SCREEN = "fromScreen"
            const val EMAIL: String = "email"
            const val NAME: String = "name"
            const val PASS: String = "pass"
            const val TYPE = "type"
            const val REMINDER = "reminder"
            const val DATA = "data"
        }

    }


    class NotificationConstants {
        companion object {
const val ADMIN_USER_ACCOUNT_VERIFY = "ADMIN_USER_ACCOUNT_VERIFY"
        }
    }


    companion object {
        const val DATA_LIMIT = 100
        const val DATA = "data"
        const val SCREEN_TYPE = "screen_type"
        const val EMAIL = "email"
        const val android = "Android"
        const val SHOW_TAG = "1"
        const val HIDE_TAG = "2"
        const val LOGIN = "login"
        const val ALLOWED_FILE_SIZE = 10
        const val ISO_FORMATE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val SELECT_TESTING_TIME = "Choose Testing Time"
        const val AFTER_MEAL_TEXT = "After Meal"
        const val AFTER_MEAL = "AFTER_MEAL"
        const val DISPLAY_ALL = "DISPLAY_ALL"
        const val THIS_WEEK = "WEEK"
        const val THIS_MONTH = "MONTH"
        const val BEFORE_MEAL_TEXT = "Before Meal"
        const val BEFORE_MEAL = "BEFORE_MEAL"
        const val CONTROLE_SOLUTION_TEXT = "Control Solution"
        const val CONTROLE_SOLUTION = "CONTROLE_SOLUTION"
        const val POST_MEDICINE_TEXT = "Post Medicine"
        const val POST_MEDICINE = "POST_MEDICINE"
        const val POST_WORKOUT_TEXT = "Post Workout"
        const val POST_WORKOUT = "POST_WORKOUT"
        const val NEVER_TEXT = "Never"
        const val NEVER = "NEVER"
        const val DAILY = "Daily"
        const val WEEKLY = "Weekly"
        const val MONTHLY = "Monthly"
        const val END_REPEAT_DATE = "End repeat date"
        const val TODAY = "TODAY"
        const val WEEK = "WEEK"
        const val MONTH = "MONTH"
        const val FROM = "from"
        const val MEAL = "meal"
        const val FOOD = "food"
        const val MEDICINE = "medicine"
        const val DIABETES = "diabetes"
        const val EVERY_DAY = "EVERY_DAY"
        const val EVERY_MONTH = "EVERY_MONTH"
        const val EVERY_WEEK = "EVERY_WEEK"
        const val SET_UP = "SET_UP"
        const val BLOOD_GLUCOSE: String = "BLOOD_GLUCOSE"
        const val INSULIN: String = "INSULIN"
        const val MG = "MG"
        const val PILLES = "PILLES"
        const val NAME = "name"
        const val UNIT = "unit"
        const val QUANTITY = "quantity"
    }

    object ContentManagementUrl {
        const val PRIVACY_POLICY_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "PRIVACY_POLICY"
        const val TERM_AND_COND_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "TERMS_AND_CONDITIONS"
        const val ABOUT_US_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "ABOUT_US"
        const val FAQ_URL = BuildConfig.ADMIN_STATIC_BASE_URL + "CONTACT_US"
    }

    object DateFormat {
        const val DATE_FORMAT_1 = "MMM dd YYYY | hh:mma"
        const val DATE_FORMAT_2 = "yyyy-MM-dd hh:mm a"
        const val DATE_FORMAT_3 = "yyyy-MM-dd"
        const val DATE_FORMAT_4 = "MMM dd YYYY"
    }


}