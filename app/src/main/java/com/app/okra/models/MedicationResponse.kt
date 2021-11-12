package com.app.okra.models


class AddMedicationRequest(
    var date: String?=null,
    var image: String?=null,
    var foodType: String?=null,
    var calories: CommonData?=null,
    var carbs: CommonData?=null,
    var fat: CommonData?=null,
    var protien: CommonData?=null,
    var foodItems: ArrayList<FoodItemsRequest>?=null,
)

class MedicationUpdateRequest(
    var mealsId: String?= null,
    var date: String?=null,
    var image: String?=null,
    var foodType: String?=null,
    var calories: CommonData?=null,
    var carbs: CommonData?=null,
    var fat: CommonData?=null,
    var protien: CommonData?=null,
   // var foodItems: ArrayList<FoodItemsRequest>?=null,
)

class MedicationResponse(
    var data: ArrayList<MedicationData>?= null
)

class MedicationData(
        var _id: String?= null,
        var userId: String?= null,
        var created: Long?=null,
        var createdAt: String?=null,
        var updatedAt: String?=null,
        var image: List<String>?=null,
        var isDeleted: Boolean?=null,
        var tags: String?=null,
        var feelings: String?=null,
        var medicineName: String?=null,
        var unit: String?=null,
        var quantity: Int?=null,
        )