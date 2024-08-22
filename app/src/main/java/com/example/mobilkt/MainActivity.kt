package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var userPref: LoggedInUserPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        if (userPref.preference.contains(LoggedInUserPref.userId)) {
            val user = userPref.getLoggedInUser()
            var intent : android.content.Intent? = null
            if (user.group == "ADMIN") {
                intent = android.content.Intent(this, MenuActivity::class.java)
            }
            if (user.group == "USER") {
                intent = android.content.Intent(this, PenggunaActivity::class.java)
            }
            if (user.group == "DRIVER") {
                intent = android.content.Intent(this, DriverActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

        val start = binding.img1

        start.setOnClickListener {
            if (userPref.preference.contains(LoggedInUserPref.userId)) {
                val user = userPref.getLoggedInUser()
                var intent : android.content.Intent? = null
                if (user.group == "ADMIN") {
                    intent = android.content.Intent(this, MenuActivity::class.java)
                }
                if (user.group == "USER") {
                    intent = android.content.Intent(this, PenggunaActivity::class.java)
                }
                if (user.group == "DRIVER") {
                    intent = android.content.Intent(this, DriverActivity::class.java)
                }
                startActivity(intent)
                finish()
            } else {
                val Intent = android.content.Intent(this, LoginActivity::class.java)
                startActivity(Intent)
            }
        }
    }
}