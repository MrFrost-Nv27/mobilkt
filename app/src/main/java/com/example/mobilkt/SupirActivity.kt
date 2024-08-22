package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.KotaAdapter
import com.example.mobilkt.data.SupirAdapter
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.databinding.ActivitySupirBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SupirActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupirBinding

    private lateinit var supirList  : ArrayList<LoggedInUser>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySupirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("user")
        supirList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                supirList.clear()
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(LoggedInUser::class.java)
                        if (data?.group == "DRIVER") {
                            supirList.add(data)
                        }
                    }
                }
                val dataAdapter = SupirAdapter(this@SupirActivity, supirList)
                binding.rvMobil.adapter = dataAdapter
                Toast.makeText(this@SupirActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SupirActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvMobil.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SupirActivity)
        }

        binding.btnAdd.setOnClickListener {
            val Intent = android.content.Intent(this, EditSupirActivity::class.java)
            startActivity(Intent)
        }
    }
}