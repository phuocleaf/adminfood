package com.example.foodapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.adapters.OrderAdapter
import com.example.foodapp.databinding.ActivityOrdersBinding
import com.example.foodapp.model.Order
import com.example.foodapp.onclick.onClickInterface
import com.example.foodapp.viewmodel.OrdersViewModel
import io.paperdb.Paper

private lateinit var binding: ActivityOrdersBinding
private lateinit var orderAdapter: OrderAdapter
private lateinit var viewModel: OrdersViewModel
private var orderList = ArrayList<Order>()

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

        initView()
    }

    private fun initView() {

        Paper.init(this)
        var id = Paper.book().read<Int>("user_id", 0)

        //intent.putExtra("status", 3)
        var status = intent.getIntExtra("status", 0)

        if(status == 1){
            binding.tvordername.text = "Đơn hàng đang được xử lý"
        }else if(status == 2){
            binding.tvordername.text = "Đơn hàng đang được giao"
        } else if(status == 3){
            binding.tvordername.text = "Đơn hàng đã giao"
        }

        viewModel.setOrders(status)
        viewModel.getCategories()
        observerLiveData()

    }

    private fun observerLiveData() {
        viewModel.observerCategoriesLiveData().observe(this) {
            orderList = it as ArrayList<Order>
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        orderAdapter = OrderAdapter(object : onClickInterface {
            override fun onClick(position: Int) {
                val order = orderAdapter.getOrderAt(position)
                val intent = Intent(this@OrdersActivity, DetailOrderActivity::class.java)
                intent.putExtra("order_id", order.order_id)
                intent.putExtra("order_total", order.order_total)
                intent.putExtra("order_datetime", order.order_datetime)
                intent.putExtra("order_status", order.order_status)
                startActivity(intent)
            }
        })
        orderAdapter.setDataOrder(orderList)
        binding.rvOrder.layoutManager = LinearLayoutManager(this)
        binding.rvOrder.adapter = orderAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}