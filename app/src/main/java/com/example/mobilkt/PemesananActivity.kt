package com.example.mobilkt

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.ActivityEditJalurBinding
import com.example.mobilkt.databinding.ActivityEditKotaBinding
import com.example.mobilkt.databinding.ActivityEditMobilBinding
import com.example.mobilkt.databinding.ActivityPemesananBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.api.IMapController
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PemesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemesananBinding
    private val calendar = Calendar.getInstance()
    lateinit var userPref: LoggedInUserPref

    private lateinit var firebaseRef : DatabaseReference
    private lateinit var firebaseJalurRef : DatabaseReference
    private lateinit var firebaseUserRef : DatabaseReference

    private lateinit var jalur  : Jalur
    private lateinit var user  : LoggedInUser
    private lateinit var supir  : LoggedInUser

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView
    private lateinit var mapController : IMapController
    private lateinit var marker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        binding = ActivityPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras

        userPref = LoggedInUserPref(this)

        firebaseRef = FirebaseDatabase.getInstance().getReference("pemesanan")
        firebaseUserRef = FirebaseDatabase.getInstance().getReference("user")
        firebaseJalurRef = FirebaseDatabase.getInstance().getReference("jalur")

        map = binding.map
        mapController = map.controller

        bundle?.let {
            bundle.apply {
                map.setTileSource(TileSourceFactory.MAPNIK)
                map.setMultiTouchControls(true)
                val dataId: String? = getString("dataId")
                if (dataId != null) {
                    val uidRef = firebaseJalurRef.child(dataId)
                    uidRef.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val snapshot = it.result.getValue<Jalur>(Jalur::class.java)
                            jalur = snapshot!!
                            binding.dari.setText(jalur.dari)
                            binding.sampai.setText(jalur.sampai)
                            binding.mobil.setText(jalur.mobil)
                            binding.supir.setText(jalur.supir)
                            binding.tarif.setText(jalur.harga)
                        }
                    }
                }
                mapController.setZoom(18)
                val startPoint = GeoPoint(-2.920192 , 132.267031)
                mapController.setCenter(startPoint)

                marker = Marker(map)
                marker.position = GeoPoint(-2.920192 , 132.267031)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Titik Jemput"
                marker.isDraggable = true
                map.overlays.add(marker)
            }
        }

        firebaseUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val d = userSnapshot.getValue(LoggedInUser::class.java)
                        if (d?.userId == userPref.getLoggedInUser().userId) user = d!!
                        if (d?.displayName == jalur.supir) supir = d
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PemesananActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.tanggal.setOnClickListener {
            showDatePicker()
        }

        binding.btnPesan.setOnClickListener {
            var dataId = firebaseRef.push().key!!
            val dari = binding.dari.text.toString()
            val sampai = binding.sampai.text.toString()
            val mobil = binding.mobil.text.toString()
            val supirVal = binding.supir.text.toString()
            val tarif = binding.tarif.text.toString().toInt()
            val tanggal = binding.tanggal.text.toString()
            val data = Pemesanan(dataId, user.userId!!, user.displayName!!, user.email!!, user.hp!!, tarif, jalur.id!!, mobil, supir.userId!!, supir.displayName!!, dari, sampai, tanggal, 0,marker.position.latitude,marker.position.longitude  )

            if (tanggal == "Pilih Tanggal") {
                binding.tanggal.error = "Pilih tanggal terlebih dahulu"
                Toast.makeText(this, "Terdapat error pada input", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseRef.child(dataId).setValue(data)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data Berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.tanggal.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }
}