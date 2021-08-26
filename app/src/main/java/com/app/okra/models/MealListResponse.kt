package com.app.okra.models

class MealListResponse(
        var data: ArrayList<MealsData>?= null
)

class MealsData(
        var _id: String?= null,
        var created: String?=null,
        var isDeleted: Boolean?=null,
        var date: String?=null,
        var image: String?=null,
        var userId: String?=null,
        var updatedAt: String?=null,
        var createdAt: String?=null,
        var foodType: FoodType?=null,
        var calories: FoodType?=null,
        var carbs: FoodType?=null,
        var fat: FoodType?=null,
        var protein: FoodType?=null,
        var foodItems: FoodItems?=null
        )

class FoodType(
        var value: String?= null,
        var unit: String?=null
)

class FoodItems(
        var _id: String?= null,
        var updatedAt: String?=null,
        var createdAt: String?=null,
        var item: String?= null,
        var type: String?=null,
        var servingSize: String?=null
)