package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.KotaAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.databinding.ActivityJalurBinding
import com.example.mobilkt.databinding.ActivityKotaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JalurActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJalurBinding

    private lateinit var jalurList  : ArrayList<Jalur>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJalurBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                val dataAdapter = JalurAdapter(this@JalurActivity, jalurList)
                binding.rvMobil.adapter = dataAdapter
                Toast.makeText(this@JalurActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@JalurActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvMobil.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@JalurActivity)
        }

        binding.btnAdd.setOnClickListener {
            val Intent = android.content.Intent(this, EditJalurActivity::class.java)
            startActivity(Intent)
        }
    }
}