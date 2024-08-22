package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilkt.data.JalurAdapter
import com.example.mobilkt.data.JalurPemesananAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.databinding.ActivityPenggunaBinding
import com.example.mobilkt.databinding.ActivityProfilBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var user  : LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        binding.btnLogout.setOnClickListener {
            userPref.removeLoggedInUser()
            val Intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }

        firebaseRef = FirebaseDatabase.getInstance().getReference("user")

        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val data = dataSnapshot.getValue(LoggedInUser::class.java)
                        if (data?.userId == userPref.getLoggedInUser().userId) user = data!!
                    }
                }
                binding.nama.setText(user.displayName)
                binding.email.setText(user.email)
                binding.hp.setText(user.hp)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfilActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.btnHome.setOnClickListener {
            val Intent = android.content.Intent(this, PenggunaActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnRiwayat.setOnClickListener {
            val Intent = android.content.Intent(this, RiwayaPenggunaActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}