package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilkt.data.GroupEnum
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.databinding.ActivityRegisterBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance().getReference("user")

        binding.btnForgot.setOnClickListener {
            val Intent = android.content.Intent(this, ForgotActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnLogin.setOnClickListener {
            val Intent = android.content.Intent(this, LoginActivity::class.java)
            startActivity(Intent)
            finish()
        }

        binding.btnRegistrar.setOnClickListener {
            val nama = binding.inputNama.text.toString()
            val email = binding.inputEmail.text.toString()
            val hp = binding.inputHp.text.toString()
            val password = binding.inputPassword.text.toString()
            val password_confirm = binding.inputPasswordConfirm.text.toString()

            if (nama.isEmpty() || email.isEmpty() || hp.isEmpty() || password.isEmpty() || password_confirm.isEmpty() || password != password_confirm) {
                if (nama.isEmpty()) binding.inputNama.error = "Tuliskan Nama"
                if (email.isEmpty()) binding.inputEmail.error = "Tuliskan Email"
                if (hp.isEmpty()) binding.inputHp.error = "Tuliskan Nomor HP"
                if (password.isEmpty()) binding.inputPassword.error = "Tuliskan Password"
                if (password_confirm.isEmpty()) binding.inputPasswordConfirm.error = "Tuliskan Konfirmasi Password"
                if (password != password_confirm) binding.inputPasswordConfirm.error = "Password dan konfirmasi password tidak sama"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId = firebaseRef.push().key!!
            val newUser = LoggedInUser(userId, nama, GroupEnum.USER.toString(), email, hp, password)

            firebaseRef.child(userId).setValue(newUser)
                .addOnCompleteListener {
                    Toast.makeText(this, "Berhasil Melakukan Pendaftaran, silahkan login", Toast.LENGTH_SHORT).show()
                    val Intent = android.content.Intent(this, LoginActivity::class.java)
                    startActivity(Intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}