package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.MobilAdapter
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityMobilBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MobilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMobilBinding

    private lateinit var mobilList  : ArrayList<Mobil>
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("mobil")
        mobilList = arrayListOf()

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mobilList.clear()
                if (snapshot.exists()) {
                    for (mobilSnapshot in snapshot.children) {
                        val mobil = mobilSnapshot.getValue(Mobil::class.java)
                        mobilList.add(mobil!!)
                    }
                }
                val mobilAdapter = MobilAdapter(this@MobilActivity, mobilList)
                binding.rvMobil.adapter = mobilAdapter
                Toast.makeText(this@MobilActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MobilActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvMobil.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MobilActivity)
        }

        binding.btnAdd.setOnClickListener {
            val Intent = android.content.Intent(this, EditMobilActivity::class.java)
            startActivity(Intent)
        }
    }
}