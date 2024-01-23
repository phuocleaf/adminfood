package com.example.foodapp.model

data class Order(
    val order_address: String,
    val order_datetime: String,
    val order_id: Int,
    val order_status: Int,
    val order_total: Int,
    val user_name: String,
    val user_phone: String
)