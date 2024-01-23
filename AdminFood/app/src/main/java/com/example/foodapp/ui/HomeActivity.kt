package com.example.foodapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeBinding
import com.example.foodapp.ui.MainActivity
import io.paperdb.Paper

private lateinit var binding: ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            showAlertDialog()
        }

        binding.llOrderProcessing.setOnClickListener{
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("status", 1)
            startActivity(intent)
        }

        binding.llOrderDelivery.setOnClickListener{
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("status", 2)
            startActivity(intent)
        }

        binding.llOrderDelivered.setOnClickListener{
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("status", 3)
            startActivity(intent)
        }

    }

    private fun showAlertDialog() {
        // Tạo một AlertDialog.Builder
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Thiết lập các thuộc tính cho AlertDialog
        alertDialogBuilder.setTitle("Đăng xuất?")
        alertDialogBuilder.setMessage("Bạn muốn đăng xuất?")

        // Thiết lập nút đóng
        alertDialogBuilder.setPositiveButton("Đóng") { dialog, which ->
            // Khi người dùng click vào nút "Đóng"
            dialog.dismiss() // Đóng AlertDialog
        }

        // Thiết lập nút đăng xuất
        alertDialogBuilder.setNegativeButton("Đăng xuất") { dialog, which ->
            Paper.book().write("admin_id", 0)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Tạo và hiển thị AlertDialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}