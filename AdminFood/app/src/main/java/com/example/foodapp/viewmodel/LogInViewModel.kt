package com.example.foodapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodapp.retrofit.RetrofitClient

class LogInViewModel(): ViewModel(){
    private var logIn: Int = 0


    fun logIn(email: String, password: String) {
        RetrofitClient.api.checkAdmin(email, password).enqueue(object : retrofit2.Callback<Int> {
            override fun onResponse(call: retrofit2.Call<Int>, response: retrofit2.Response<Int>) {
                response.body()?.let { status ->
                    logIn = status
                }
            }

            override fun onFailure(call: retrofit2.Call<Int>, t: Throwable) {
                logIn = -1
            }
        })
    }

    fun getLogInStatus(): Int {
        return logIn
    }

}