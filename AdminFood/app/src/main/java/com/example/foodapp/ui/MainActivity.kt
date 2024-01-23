package com.example.foodapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.R
import io.paperdb.Paper
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                Paper.init(this@MainActivity)
                var adminId = Paper.book().read("admin_id", 0)
                if(adminId == 0) {
                    val intent = Intent(this@MainActivity, LogInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 3000)
    }
}