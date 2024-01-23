package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.model.Detail
import com.example.foodapp.model.Details
import com.example.foodapp.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsOrderViewModel(): ViewModel() {
    private var orderId = 0

    private var detailsLiveData = MutableLiveData<List<Detail>>()

    fun setOrderId(id: Int){
        orderId = id
    }

    fun getDetails(){
        RetrofitClient.api.getDetails(orderId).enqueue(object : Callback<Details> {
            override fun onResponse(call: Call<Details>, response: Response<Details>) {
                response.body()?.let {detail1 ->
                    detailsLiveData.postValue(detail1.details)
                }
            }

            override fun onFailure(call: Call<Details>, t: Throwable) {
                Log.e("logg",t.message.toString())
            }

        })
    }
    fun observerDetailsLiveData(): LiveData<List<Detail>> {
        return detailsLiveData
    }
}