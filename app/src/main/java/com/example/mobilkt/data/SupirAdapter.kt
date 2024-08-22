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
import com.example.mobilkt.EditKotaActivity
import com.example.mobilkt.EditMobilActivity
import com.example.mobilkt.EditSupirActivity
import com.example.mobilkt.R
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.LoggedInUser
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.KotaItemBinding
import com.example.mobilkt.databinding.MobilItemBinding
import com.example.mobilkt.databinding.SupirItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SupirAdapter(
    private val context: Context,
    private val data: java.util.ArrayList<LoggedInUser>
) : RecyclerView.Adapter<SupirAdapter.ViewHolder>() {

    private lateinit var firebaseRef : DatabaseReference

    class ViewHolder(val binding : SupirItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupirAdapter.ViewHolder {
        return ViewHolder(SupirItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SupirAdapter.ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.apply {
            binding.apply {
                namaItem.text = currentItem.displayName
                btnEdit.setOnClickListener {
                    val intent = android.content.Intent(context, EditSupirActivity::class.java)
                    intent.putExtra("dataId", currentItem.userId)
                    context.startActivity(intent)
                }
                btnDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Apakah anda yakin ingin menghapus data ini ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya") { dialog, id ->
                            firebaseRef = FirebaseDatabase.getInstance().getReference("user")
                            firebaseRef.child(currentItem.userId!!).removeValue()
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
        return data.size
    }
}