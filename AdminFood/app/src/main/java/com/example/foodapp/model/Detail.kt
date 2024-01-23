package com.example.foodapp.model

data class Detail(
    val detail_id: Int,
    val detail_quantity: Int,
    val food_id: Int,
    val food_image: String,
    val food_name: String,
    val food_price: Int,
    val order_id: Int
)