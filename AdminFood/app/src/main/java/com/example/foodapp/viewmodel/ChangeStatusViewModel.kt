package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodapp.retrofit.RetrofitClient

class ChangeStatusViewModel(): ViewModel() {

    private var status: Int = 0


    fun changeOrderStatus(orderId: Int,orderStatus: Int) {
        RetrofitClient.api.changeStatus(orderId, orderStatus).enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: retrofit2.Call<Int>, response: retrofit2.Response<Int>) {
                response.body()?.let { statuss ->
                    status = statuss
                }
            }

            override fun onFailure(call: retrofit2.Call<Int>, t: Throwable) {
                status = -1
            }
        })
    }

    fun getChangeStatus(): Int {
        return status
    }
}