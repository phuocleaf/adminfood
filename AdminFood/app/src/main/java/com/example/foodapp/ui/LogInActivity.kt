package com.example.foodapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.databinding.ActivityLogInBinding
import com.example.foodapp.ui.HomeActivity
import com.example.foodapp.viewmodel.LogInViewModel
import io.paperdb.Paper

private lateinit var binding: ActivityLogInBinding
private lateinit var viewModel: LogInViewModel


class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LogInViewModel::class.java]

        binding.btnlogin.setOnClickListener{

            viewModel.logIn(binding.etemail.text.toString(),binding.etpassword.text.toString())

            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                var status = viewModel.getLogInStatus()
                if(status != 0){
                    Paper.init(this)
                    Paper.book().write("admin_id", status)
                    Toast.makeText(this,"Đăng nhập thành công", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else{
                    Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show()
                }
            }, 1000) //

        }
    }
}