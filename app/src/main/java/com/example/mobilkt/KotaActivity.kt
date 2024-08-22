package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.KotaAdapter
import com.example.mobilkt.data.MobilAdapter
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityKotaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKotaBinding

    private lateinit var kotaList  : ArrayList<Kota>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("kota")
        kotaList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kotaList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(Kota::class.java)
                        kotaList.add(data!!)
                    }
                }
                val dataAdapter = KotaAdapter(this@KotaActivity, kotaList)
                binding.rvMobil.adapter = dataAdapter
                Toast.makeText(this@KotaActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@KotaActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvMobil.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@KotaActivity)
        }

        binding.btnAdd.setOnClickListener {
            val Intent = android.content.Intent(this, EditKotaActivity::class.java)
            startActivity(Intent)
        }
    }
}