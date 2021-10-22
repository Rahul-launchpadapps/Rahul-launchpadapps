package com.app.okra.models

class HomeResponse(
        var graphInfo: ArrayList<GraphInfo>?= null,
        var totalTest: String?=null,
        var avgBloodGlucose: String?=null,
        var avgInsulin: String?=null,
        var hyper_hypes: HyperHypes?=null,
        var Est_HbA1c: String?=null,
        var carbsCount: String?=null,
        var foodLogs: ArrayList<MealData>?=null
        )

class GraphInfo(
        var _id: Int?=null,
        var bloodGlucose: String?=null,
        var hours: String?=null
)

class HyperHypes(
        var hyper: String?=null,
        var hypos: String?=null
)

class FoodLogs(
        var _id: String?=null,
        var calories: CommonData?=null,
        var carbs: CommonData?=null,
        var foodItems: ArrayList<FoodItems>?=null,
        var foodType: String?=null
)