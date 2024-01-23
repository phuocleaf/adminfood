package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodapp.retrofit.RetrofitClient

class DeleteOrderViewModel(): ViewModel() {

    private var stat: Int = 0


    fun deleteOrder(orderId: Int) {
        RetrofitClient.api.deleteOrder(orderId) .enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: retrofit2.Call<Int>, response: retrofit2.Response<Int>) {
                response.body()?.let { status ->
                    stat = status
                }
            }

            override fun onFailure(call: retrofit2.Call<Int>, t: Throwable) {
                stat = -1
            }
        })
    }

    fun getDeleteStatus(): Int {
        return stat
    }
}