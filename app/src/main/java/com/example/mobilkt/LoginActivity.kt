package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobilkt.data.GroupEnum
import com.example.mobilkt.data.MobilAdapter
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseRef : DatabaseReference
    lateinit var userPref: LoggedInUserPref

    private lateinit var userList  : ArrayList<LoggedInUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        firebaseRef = FirebaseDatabase.getInstance().getReference("user")
        userList = arrayListOf()

        binding.btnForgot.setOnClickListener {
            val Intent = android.content.Intent(this, ForgotActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnRegister.setOnClickListener {
            val Intent = android.content.Intent(this, RegisterActivity::class.java)
            startActivity(Intent)
            finish()
        }

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(LoggedInUser::class.java)
                        userList.add(user!!)
                    }
                }
                binding.login.isEnabled = true
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.login.setOnClickListener {
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty() || password.length < 3) {
                if (email.isEmpty()) binding.username.error = "Tuliskan Email"
                if (password.isEmpty()) binding.password.error = "Tuliskan Password"
                if (password.length < 3) binding.password.error = "Password minimal terdiri 3 karakter"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var found = false
            var userRetrieve : LoggedInUser? = null

            userList.forEach {
                if (it.email == email) {
                    found = true
                    userRetrieve = it
                }
            }

            if (found == false) {
                Toast.makeText(this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                if (password != userRetrieve?.password) {
                    Toast.makeText(this, "Kata sandi salah", Toast.LENGTH_SHORT).show()
                } else {
                    userPref.setLoggedInUser(userRetrieve!!)
                    var Intent : android.content.Intent? = null;
                    if (userRetrieve!!.group == "ADMIN") {
                        Intent = android.content.Intent(this, MenuActivity::class.java)
                    }
                    if (userRetrieve!!.group == "USER") {
                        Intent = android.content.Intent(this, PenggunaActivity::class.java)
                    }
                    if (userRetrieve!!.group == "DRIVER") {
                        Intent = android.content.Intent(this, DriverActivity::class.java)
                    }
                    startActivity(Intent)
                    finish()
                }
            }
        }
    }
}