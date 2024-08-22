package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.RiwayatAdapter
import com.example.mobilkt.data.TransaksiAdapter
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.ActivityProsesBinding
import com.example.mobilkt.databinding.ActivityRiwayatBinding
import com.example.mobilkt.databinding.ActivityTransaksiBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var pemesananList  : ArrayList<Pemesanan>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRiwayatBinding.inflate(layoutInflater)
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
                        if (data?.status!! > 0) pemesananList.add(
                            data
                        )
                    }
                }
                val dataAdapter = RiwayatAdapter(this@RiwayatActivity, pemesananList)
                binding.rvData.adapter = dataAdapter
                Toast.makeText(this@RiwayatActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RiwayatActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@RiwayatActivity)
        }
    }
}