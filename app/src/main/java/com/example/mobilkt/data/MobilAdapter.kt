package com.example.mobilkt.data

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilkt.EditMobilActivity
import com.example.mobilkt.R
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.MobilItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MobilAdapter(
    private val context: Context,
    private val mobilList: java.util.ArrayList<Mobil>
) : RecyclerView.Adapter<MobilAdapter.ViewHolder>() {

    private lateinit var firebaseRef : DatabaseReference

    class ViewHolder(val binding : MobilItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobilAdapter.ViewHolder {
        return ViewHolder(MobilItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MobilAdapter.ViewHolder, position: Int) {
        val currentItem = mobilList[position]
        holder.apply {
            binding.apply {
                namaMobil.text = currentItem.nama
                hargaMobil.text =  currentItem.harga
                nomorMobil.text = currentItem.nomor
                btnEdit.setOnClickListener {
                    val intent = android.content.Intent(context, EditMobilActivity::class.java)
                    intent.putExtra("mobilId", currentItem.id)
                    context.startActivity(intent)
                }
                btnDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Apakah anda yakin ingin menghapus data ini ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya") { dialog, id ->
                            firebaseRef = FirebaseDatabase.getInstance().getReference("mobil")
                            firebaseRef.child(currentItem.id!!).removeValue()
                                .addOnCompleteListener {
                                    Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("Tidak") { dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mobilList.size
    }
}