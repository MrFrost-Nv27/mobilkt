package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.JalurPemesananAdapter
import com.example.mobilkt.data.PemesananAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.ActivityPenggunaBinding
import com.example.mobilkt.databinding.ActivityRiwayatPenggunaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RiwayaPenggunaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPenggunaBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var pemesananList  : ArrayList<Pemesanan>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRiwayatPenggunaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        binding.btnLogout.setOnClickListener {
            userPref.removeLoggedInUser()
            val Intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("pemesanan")
        pemesananList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pemesananList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(Pemesanan::class.java)
                        if (data?.idUser == userPref.getLoggedInUser().userId) pemesananList.add(data!!)
                    }
                }
                val dataAdapter = PemesananAdapter(this@RiwayaPenggunaActivity, pemesananList)
                binding.rvData.adapter = dataAdapter
                Toast.makeText(this@RiwayaPenggunaActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RiwayaPenggunaActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RiwayaPenggunaActivity)
        }

        binding.btnHome.setOnClickListener {
            val Intent = android.content.Intent(this, PenggunaActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnProfil.setOnClickListener {
            val Intent = android.content.Intent(this, ProfilActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}