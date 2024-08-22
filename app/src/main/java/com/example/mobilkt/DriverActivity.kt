package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.JalurPemesananAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.databinding.ActivityDriverBinding
import com.example.mobilkt.databinding.ActivityPenggunaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DriverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        binding.btnLogout.setOnClickListener {
            userPref.removeLoggedInUser()
            val Intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }

        binding.btnProses.setOnClickListener {
            val Intent = android.content.Intent(this, ProsesActivity::class.java)
            startActivity(Intent)
        }
        binding.btnLokasi.setOnClickListener {
            val Intent = android.content.Intent(this, LokasiActivity::class.java)
            startActivity(Intent)
        }
        binding.btnChat.setOnClickListener {
            val Intent = android.content.Intent(this, ChatActivity::class.java)
            startActivity(Intent)
        }
    }
}