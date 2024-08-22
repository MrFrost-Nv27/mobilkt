package com.example.mobilkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.Toast
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityEditMobilBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue

class EditMobilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMobilBinding

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMobilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        var dataMobil : Mobil? = null

        firebaseRef = FirebaseDatabase.getInstance().getReference("mobil")

        bundle?.let {
            bundle.apply {
                val mobilId: String? = getString("mobilId")
                if (mobilId != null) {
                    val uidRef = firebaseRef.child(mobilId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<Mobil>(Mobil::class.java)
                            dataMobil = snapshot
                            binding.editNamaMobil.setText(snapshot?.nama)
                            binding.editHargaMobil.setText(snapshot?.harga)
                            binding.editNomorMobil.setText(snapshot?.nomor)
                        }
                    }
                }
            }
        }

        binding.btnEditMobilSave.setOnClickListener {
            val nama = binding.editNamaMobil.text.toString()
            val harga = binding.editHargaMobil.text.toString()
            val nomor = binding.editNomorMobil.text.toString()

            if (nama.isEmpty() || harga.isEmpty() || nomor.isEmpty()) {
                if (nama.isEmpty()) binding.editNamaMobil.error = "Tuliskan Nama Mobil"
                if (harga.isEmpty()) binding.editHargaMobil.error = "Tuliskan Harga Mobil"
                if (nomor.isEmpty()) binding.editNomorMobil.error = "Tuliskan Nomor Mobil"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dataMobil != null) {
                Toast.makeText(this, dataMobil?.nama, Toast.LENGTH_SHORT).show()
                dataMobil?.nama = nama
                dataMobil?.harga = harga
                dataMobil?.nomor = nomor

                firebaseRef.child(dataMobil?.id!!).setValue(dataMobil)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data Mobil Berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val mobilId = firebaseRef.push().key!!
                val mobil = Mobil(mobilId, nama, harga, nomor)

                firebaseRef.child(mobilId).setValue(mobil)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data Mobil Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}