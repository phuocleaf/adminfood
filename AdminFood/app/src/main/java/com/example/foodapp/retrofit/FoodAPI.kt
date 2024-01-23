package com.example.foodapp.retrofit

import com.example.foodapp.model.Details
import com.example.foodapp.model.Orders
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodAPI {
    @POST("/admin/check")
    fun checkAdmin(
        @Query("admin_email") email: String,
        @Query("admin_password") password: String
    ): Call<Int>

    @GET("/full_order/{order_status}")
    fun getOrders(
        @Path("order_status") status: Int
    ): Call<Orders>

    @GET("/detail/{order_id}")
    fun getDetails(
        @Path("order_id") id: Int,
    ): Call<Details>

    @PUT("/change_status/{order_id}/{status}")
    fun changeStatus(
        @Path("order_id") id: Int,
        @Path("status") status: Int
    ): Call<Int>

    @DELETE("/delete/{order_id}")
    fun deleteOrder(
        @Path("order_id") id: Int
    ): Call<Int>

}