package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.JalurPemesananAdapter
import com.example.mobilkt.data.LokasiAdapter
import com.example.mobilkt.data.PemesananAdapter
import com.example.mobilkt.data.ProsesAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.ActivityLokasiBinding
import com.example.mobilkt.databinding.ActivityPenggunaBinding
import com.example.mobilkt.databinding.ActivityRiwayatPenggunaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LokasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLokasiBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var pemesananList  : ArrayList<Pemesanan>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        firebaseRef = FirebaseDatabase.getInstance().getReference("pemesanan")
        pemesananList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pemesananList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(Pemesanan::class.java)
                        if (data?.supirId == userPref.getLoggedInUser().userId && data?.status!! == 1) pemesananList.add(data!!)
                    }
                }
                val dataAdapter = LokasiAdapter(this@LokasiActivity, pemesananList)
                binding.rvData.adapter = dataAdapter
                Toast.makeText(this@LokasiActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LokasiActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LokasiActivity)
        }
    }
}