package com.example.mobilkt

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras

        userPref = LoggedInUserPref(this)

        firebaseRef = FirebaseDatabase.getInstance().getReference("pemesanan")
        firebaseUserRef = FirebaseDatabase.getInstance().getReference("user")
        firebaseJalurRef = FirebaseDatabase.getInstance().getReference("jalur")

        bundle?.let {
            bundle.apply {
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
            val data = Pemesanan(dataId, user.userId!!, user.displayName!!, user.email!!, user.hp!!, tarif, jalur.id!!, mobil, supir.userId!!, supir.displayName!!, dari, sampai, tanggal, 0  )

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
}