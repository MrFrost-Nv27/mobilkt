package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilkt.data.GroupEnum
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.ActivityEditKotaBinding
import com.example.mobilkt.databinding.ActivityEditMobilBinding
import com.example.mobilkt.databinding.ActivityEditSupirBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditSupirActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSupirBinding

    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditSupirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        var dataSupir : LoggedInUser? = null

        firebaseRef = FirebaseDatabase.getInstance().getReference("user")

        bundle?.let {
            bundle.apply {
                val dataId: String? = getString("dataId")
                if (dataId != null) {
                    val uidRef = firebaseRef.child(dataId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<LoggedInUser>(LoggedInUser::class.java)
                            dataSupir = snapshot
                            binding.editNamaSupir.setText(snapshot?.displayName)
                            binding.editEmailSupir.setText(snapshot?.email)
                            binding.editHpSupir.setText(snapshot?.hp)
                        }
                    }
                }
            }
        }

        binding.btnEditSave.setOnClickListener {
            val nama = binding.editNamaSupir.text.toString()
            val email = binding.editEmailSupir.text.toString()
            val hp = binding.editHpSupir.text.toString()
            val password = binding.editPasswordSupir.text.toString()
            val passwordConfirm = binding.editPasswordConfirmSupir.text.toString()

            var supirId : String = ""
            var data : LoggedInUser? = null

            if (dataSupir != null) {
                supirId = dataSupir?.userId!!
                if (nama.isEmpty() || email.isEmpty() || hp.isEmpty()) {
                    if (nama.isEmpty()) binding.editNamaSupir.error = "Tuliskan Nama"
                    if (email.isEmpty()) binding.editEmailSupir.error = "Tuliskan Email"
                    if (hp.isEmpty()) binding.editHpSupir.error = "Tuliskan Nomor HP"
                    Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!password.isEmpty()) {
                    if (password == passwordConfirm) {
                        dataSupir?.password = password
                    } else {
                        if (passwordConfirm.isEmpty()) binding.editPasswordConfirmSupir.error = "Tuliskan Konfirmasi Password"
                        if (password != passwordConfirm) binding.editPasswordConfirmSupir.error = "Password dan konfirmasi password tidak sama"
                        Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                dataSupir?.displayName = nama
                dataSupir?.email = email
                dataSupir?.hp = hp
                data = dataSupir
            } else {
                supirId = firebaseRef.push().key!!
                if (nama.isEmpty() || email.isEmpty() || hp.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || password != passwordConfirm) {
                    if (nama.isEmpty()) binding.editNamaSupir.error = "Tuliskan Nama"
                    if (email.isEmpty()) binding.editEmailSupir.error = "Tuliskan Email"
                    if (hp.isEmpty()) binding.editHpSupir.error = "Tuliskan Nomor HP"
                    if (password.isEmpty()) binding.editPasswordSupir.error = "Tuliskan Password"
                    if (passwordConfirm.isEmpty()) binding.editPasswordConfirmSupir.error = "Tuliskan Konfirmasi Password"
                    if (password != passwordConfirm) binding.editPasswordConfirmSupir.error = "Password dan konfirmasi password tidak sama"
                    Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                data = LoggedInUser(supirId, nama, GroupEnum.DRIVER.toString(), email, hp, password)
            }

            firebaseRef.child(supirId).setValue(data)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data Berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}