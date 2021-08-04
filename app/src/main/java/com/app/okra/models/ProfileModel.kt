package com.app.okra.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemModel(val name :String, val icon :Int)

class UserDetailResponse(){
    var userId: String?=null
    var email: String?=null
    var userType: String?=null
    var name: String?=null
    var age: String?=null
    var mobile: String?=null
}