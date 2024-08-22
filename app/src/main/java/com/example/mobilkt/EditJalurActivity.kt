package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityEditJalurBinding
import com.example.mobilkt.databinding.ActivityEditKotaBinding
import com.example.mobilkt.databinding.ActivityEditMobilBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditJalurActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditJalurBinding

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var firebaseSupirRef : DatabaseReference
    private lateinit var firebaseMobilRef : DatabaseReference
    private lateinit var firebaseKotaRef : DatabaseReference

    private lateinit var supirList  : ArrayList<LoggedInUser>
    private lateinit var mobilList  : ArrayList<Mobil>
    private lateinit var kotaList  : ArrayList<Kota>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditJalurBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        var dataJalur : Jalur? = null

        supirList = arrayListOf()
        mobilList = arrayListOf()
        kotaList = arrayListOf()

        firebaseRef = FirebaseDatabase.getInstance().getReference("jalur")
        firebaseSupirRef = FirebaseDatabase.getInstance().getReference("user")
        firebaseMobilRef = FirebaseDatabase.getInstance().getReference("mobil")
        firebaseKotaRef = FirebaseDatabase.getInstance().getReference("kota")

        bundle?.let {
            bundle.apply {
                val dataId: String? = getString("dataId")
                if (dataId != null) {
                    val uidRef = firebaseRef.child(dataId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<Jalur>(Jalur::class.java)
                            dataJalur = snapshot
                            binding.jalurTarif.setText(snapshot?.harga)
                        }
                    }
                }
            }
        }

        firebaseSupirRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                supirList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(LoggedInUser::class.java)
                        if (user?.group == "DRIVER") supirList.add(user)
                    }
                }
                val adapter = ArrayAdapter(this@EditJalurActivity,
                    android.R.layout.simple_spinner_item, supirList.map { u -> u.displayName })
                binding.jalurSupir.adapter = adapter
                if (dataJalur != null) binding.jalurSupir.setSelection(adapter.getPosition(dataJalur?.supir))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditJalurActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        firebaseMobilRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mobilList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val mobil = userSnapshot.getValue(Mobil::class.java)
                        mobilList.add(mobil!!)
                    }
                }
                val adapter = ArrayAdapter(this@EditJalurActivity,
                    android.R.layout.simple_spinner_item, mobilList.map { u -> u.nama })
                binding.jalurMobil.adapter = adapter
                if (dataJalur != null) binding.jalurMobil.setSelection(adapter.getPosition(dataJalur?.mobil))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditJalurActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        firebaseKotaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kotaList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val kota = userSnapshot.getValue(Kota::class.java)
                        kotaList.add(kota!!)
                    }
                }
                val adapter = ArrayAdapter(this@EditJalurActivity,
                    android.R.layout.simple_spinner_item, kotaList.map { u -> u.nama })
                val adapter2 = ArrayAdapter(this@EditJalurActivity,
                    android.R.layout.simple_spinner_item, kotaList.map { u -> u.nama })
                binding.jalurDari.adapter = adapter
                binding.jalurSampai.adapter = adapter2
                if (dataJalur != null) binding.jalurDari.setSelection(adapter.getPosition(dataJalur?.dari))
                if (dataJalur != null) binding.jalurSampai.setSelection(adapter.getPosition(dataJalur?.sampai))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditJalurActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.btnEditSave.setOnClickListener {
            var dataId : String? = ""
            var data : Jalur? = null
            val harga = binding.jalurTarif.text.toString()

            if (harga.isEmpty()) {
                if (harga.isEmpty()) binding.jalurTarif.error = "Tuliskan Tarif Jalur"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dataJalur != null) {
                dataId = dataJalur?.id
            } else {
                dataId = firebaseRef.push().key!!
                dataJalur = Jalur(dataId)
            }
            dataJalur?.supir = binding.jalurSupir.selectedItem.toString()
            dataJalur?.mobil = binding.jalurMobil.selectedItem.toString()
            dataJalur?.dari = binding.jalurDari.selectedItem.toString()
            dataJalur?.sampai = binding.jalurSampai.selectedItem.toString()
            dataJalur?.harga = harga
            data = dataJalur

            firebaseRef.child(dataId!!).setValue(data)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data Berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}