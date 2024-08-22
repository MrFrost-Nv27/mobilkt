package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityEditKotaBinding
import com.example.mobilkt.databinding.ActivityEditMobilBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditKotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditKotaBinding

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditKotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        var dataKota : Kota? = null

        firebaseRef = FirebaseDatabase.getInstance().getReference("kota")

        bundle?.let {
            bundle.apply {
                val dataId: String? = getString("dataId")
                if (dataId != null) {
                    val uidRef = firebaseRef.child(dataId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<Kota>(Kota::class.java)
                            dataKota = snapshot
                            binding.editNamaKota.setText(snapshot?.nama)
                        }
                    }
                }
            }
        }

        binding.btnEditMobilSave.setOnClickListener {
            val nama = binding.editNamaKota.text.toString()

            if (nama.isEmpty()) {
                if (nama.isEmpty()) binding.editNamaKota.error = "Tuliskan Nama Kota"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dataKota != null) {
                Toast.makeText(this, dataKota?.nama, Toast.LENGTH_SHORT).show()
                dataKota?.nama = nama

                firebaseRef.child(dataKota?.id!!).setValue(dataKota)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data Berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val dataId = firebaseRef.push().key!!
                val data = Kota(dataId, nama)

                firebaseRef.child(dataId).setValue(data)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data Baru Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}