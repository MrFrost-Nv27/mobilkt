package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilkt.databinding.ActivityForgotBinding

class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val Intent = android.content.Intent(this, RegisterActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnLogin.setOnClickListener {
            val Intent = android.content.Intent(this, LoginActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}