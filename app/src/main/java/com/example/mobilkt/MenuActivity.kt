package com.example.mobilkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobilkt.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    lateinit var userPref: LoggedInUserPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        val btn_mobil = binding.btnMobil
        val btn_riwayat = binding.btnRiwayat
        val btn_kota = binding.btnKota
        val btn_supir = binding.btnSupir
        val btn_transaksi = binding.btnTransaksi
        val btn_jalur = binding.btnJalur
        val btn_logout = binding.btnLogout

        btn_mobil.setOnClickListener {
            val Intent = android.content.Intent(this, MobilActivity::class.java)
            startActivity(Intent)
        }
        btn_riwayat.setOnClickListener {
            val Intent = android.content.Intent(this, RiwayatActivity::class.java)
            startActivity(Intent)
        }
        btn_kota.setOnClickListener {
            val Intent = android.content.Intent(this, KotaActivity::class.java)
            startActivity(Intent)
        }
        btn_supir.setOnClickListener {
            val Intent = android.content.Intent(this, SupirActivity::class.java)
            startActivity(Intent)
        }
        btn_transaksi.setOnClickListener {
            val Intent = android.content.Intent(this, TransaksiActivity::class.java)
            startActivity(Intent)
        }
        btn_jalur.setOnClickListener {
            val Intent = android.content.Intent(this, JalurActivity::class.java)
            startActivity(Intent)
        }

        btn_logout.setOnClickListener {
            userPref.removeLoggedInUser()
            val Intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}