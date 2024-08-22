package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.JalurPemesananAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.databinding.ActivityPenggunaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PenggunaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPenggunaBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var jalurList  : ArrayList<Jalur>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPenggunaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        binding.btnLogout.setOnClickListener {
            userPref.removeLoggedInUser()
            val Intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("jalur")
        jalurList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jalurList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(Jalur::class.java)
                        jalurList.add(data!!)
                    }
                }
                val dataAdapter = JalurPemesananAdapter(this@PenggunaActivity, jalurList)
                binding.rvData.adapter = dataAdapter
                Toast.makeText(this@PenggunaActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PenggunaActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@PenggunaActivity)
        }

        binding.btnRiwayat.setOnClickListener {
            val Intent = android.content.Intent(this, RiwayaPenggunaActivity::class.java)
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