package com.example.mobilkt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobilkt.data.PemesananAdapter
import com.example.mobilkt.data.RiwayatAdapter
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.ActivityKonfirmasiBinding
import com.example.mobilkt.databinding.ActivityRiwayatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class KonfirmasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKonfirmasiBinding
    lateinit var userPref: LoggedInUserPref

    private lateinit var pemesananList  : ArrayList<Pemesanan>
    private lateinit var firebaseRef : DatabaseReference

    private lateinit var pemesanan  : Pemesanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKonfirmasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = LoggedInUserPref(this)

        firebaseRef = FirebaseDatabase.getInstance().getReference("pemesanan")
        pemesananList = arrayListOf()

        val bundle : Bundle? = intent.extras

        bundle?.let {
            bundle.apply {
                val dataId: String? = getString("dataId")
                if (dataId != null) {
                    pemesananList.clear()
                    val uidRef = firebaseRef.child(dataId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<Pemesanan>(Pemesanan::class.java)
                            pemesanan = snapshot!!
                            pemesananList.add(pemesanan)
                            val dataAdapter = PemesananAdapter(this@KonfirmasiActivity, pemesananList)
                            binding.rvData.adapter = dataAdapter
                            Toast.makeText(this@KonfirmasiActivity, "Fetch Data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnConfirm.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=6281247489609"))
            startActivity(intent)
        }

        binding.btnHome.setOnClickListener {
            val Intent = android.content.Intent(this, PenggunaActivity::class.java)
            startActivity(Intent)
            finish()
        }
        binding.btnProfil.setOnClickListener {
            val Intent = android.content.Intent(this, ProfilActivity::class.java)
            startActivity(Intent)
            finish()
        }
    }
}