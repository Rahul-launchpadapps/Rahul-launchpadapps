package com.app.okra.models

data class DevicesListModel(val name:String,
                            val address: String,
                            val isPaired: Boolean = false)
