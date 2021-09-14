package com.app.okra.models

class FoodRecognintionResponse (
    var is_food: Boolean,
    var _timing: Timing?= null,
    var lang: String?=null,
    var imagecache_id: String?=null,
    var results: ArrayList<Results>?=null
)

class Timing (
    var foodai_totaltime: String? = null,
    var foodai_classificationtime: String?= null,
    var proxy_foodairequesttime: String?=null,
)

class Results (
    var items: ArrayList<Items>?=null,
    var group: String?= null,
)

class Items (
    var servingSizes: ArrayList<ServingSize>?=null,
    var score: Int?= null,
    var nutrition: Nutrition?= null,
    var name: String?= null,
    var food_id: String?= null,
    var group: String?= null,
)

class ServingSize (
    var unit: String?= null,
    var servingWeight: Double?= null
)

class Nutrition (
    var totalCarbs: Double?= null,
    var totalFat: Double?= null,
    var protein: Double?= null,
    var calories: Double?= null
)