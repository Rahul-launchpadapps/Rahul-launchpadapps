package com.app.okra.models

class DeviceDataCount(var deviceId: String, var testCount  :Int)

class BLETestData(
    var date: String?=null,
    var testingTime :String?=null,
    var bloodGlucose :Int?=null,
    var deviceName :String?=null,
    var deviceId :String?=null,
    var totalDataCount :String?=null,
)

class DeviceDataRequest(var deviceUUID: String?=null, var deviceName  :String?=null)

class Date(
    var day: String="00",
    var month: String="00",
    var year: String="00",
    var hour:String="00",
    var min:String="00",
    var sec:String="00",
){

    fun getCompleteDate() :String{
        return "$day-$month-$year $hour:$min:$sec"
    }

}