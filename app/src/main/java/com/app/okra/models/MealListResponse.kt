package com.app.okra.models

class MealListResponse(
        var data: ArrayList<MealData>?= null
)

class MealData(
        var _id: String?= null,
        var userId: String?= null,
        var created: Long?=null,
        var createdAt: String?=null,
        var updatedAt: String?=null,
        var isDeleted: Boolean?=null,
        var date: String?=null,
        var image: String?=null,
        var foodType: CommonData?=null,
        var calories: CommonData?=null,
        var fat: CommonData?=null,
        var protien: CommonData?=null,
        var foodItems: List<FoodItems>?=null,
        )

class CommonData(value :Int, unit: String)
class FoodItems(
        val _id :String?=null,
         val item: String,
         val type: String,
         val servingSize: String,
         val createdAt: String,
         val updatedAt: String,
)






