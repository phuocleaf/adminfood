package com.example.foodapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.DetailOrderAdapter
import com.example.foodapp.databinding.ActivityDetailOrderBinding
import com.example.foodapp.model.Detail
import com.example.foodapp.viewmodel.ChangeStatusViewModel
import com.example.foodapp.viewmodel.DeleteOrderViewModel
import com.example.foodapp.viewmodel.DetailsOrderViewModel

private lateinit var binding: ActivityDetailOrderBinding
private lateinit var viewModel: DetailsOrderViewModel
private lateinit var detailAdapter: DetailOrderAdapter
private lateinit var changeStatusViewModel: ChangeStatusViewModel
private lateinit var deleteOrderViewModel: DeleteOrderViewModel
private var orderList = ArrayList<Detail>()

class DetailOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailsOrderViewModel::class.java]

        initView()


    }

    private fun initView() {
        val orderId = intent.getIntExtra("order_id", 0)
        val total = intent.getIntExtra("order_total", 0)
        val dateTime = intent.getStringExtra("order_datetime")
        val status = intent.getIntExtra("order_status", 0)
        binding.tvorderid.text = orderId.toString()
        binding.tvordertotal.text = total.toString()
        binding.tvordertime.text = dateTime

        if(status == 2) {
            binding.btndeleteorder.visibility = View.GONE
        } else if(status == 3) {
            binding.btnchangestatus.visibility = View.GONE
            binding.btndeleteorder.visibility = View.GONE
        }

        viewModel.setOrderId(orderId)
        viewModel.getDetails()
        observerLiveData()

        binding.btnchangestatus.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có chắc chắn muốn thay đổi trạng thái đơn hàng này không?")
            builder.setPositiveButton("Có") { dialog, which ->
                changeStatusViewModel = ViewModelProvider(this)[ChangeStatusViewModel::class.java]
                changeStatusViewModel.changeOrderStatus(orderId, status + 1)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    var stat = changeStatusViewModel.getChangeStatus()
                    if(stat != 0){
                        Toast.makeText(this,"Thay đổi trạng thái đơn hàng thành công", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, OrdersActivity::class.java)
                        intent.putExtra("status", status)
                        startActivity(intent)
                        finish()
                    } else{
                        Toast.makeText(this, "Thay đổi trạng thái đơn hàng thất bại", Toast.LENGTH_LONG).show()
                    }
                }, 1000) //
            }
            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }

        binding.btndeleteorder.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có chắc chắn muốn xóa đơn hàng này không?")
            builder.setPositiveButton("Có") { dialog, which ->
                deleteOrderViewModel = ViewModelProvider(this)[DeleteOrderViewModel::class.java]
                deleteOrderViewModel.deleteOrder(orderId)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    var status = deleteOrderViewModel.getDeleteStatus()
                    if(status != 0){
                        Toast.makeText(this,"Xóa đơn hàng thành công", Toast.LENGTH_LONG).show()
                        val status = intent.getIntExtra("order_status", 0)
                        val intent = Intent(this, OrdersActivity::class.java)
                        //var status = intent.getIntExtra("status", 0)
                        intent.putExtra("status", status)
                        startActivity(intent)
                        finish()
                    } else{
                        Toast.makeText(this, "Xóa đơn hàng thất bại", Toast.LENGTH_LONG).show()
                    }
                }, 1000) //
            }
            builder.setNegativeButton("Không") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun observerLiveData() {
        viewModel.observerDetailsLiveData().observe(this) {
            orderList = it as ArrayList<Detail>
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        detailAdapter = DetailOrderAdapter()
        detailAdapter.setDataDetail(orderList)
        binding.rvdetailorder.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(this@DetailOrderActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onBackPressed() {
        val status = intent.getIntExtra("order_status", 0)
        val intent = Intent(this, OrdersActivity::class.java)
        //var status = intent.getIntExtra("status", 0)
        intent.putExtra("status", status)
        startActivity(intent)
    }
}