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
import com.example.mobilkt.EditJalurActivity
import com.example.mobilkt.EditKotaActivity
import com.example.mobilkt.EditMobilActivity
import com.example.mobilkt.PemesananActivity
import com.example.mobilkt.R
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.databinding.JalurItemBinding
import com.example.mobilkt.databinding.JalurPemesananItemBinding
import com.example.mobilkt.databinding.KotaItemBinding
import com.example.mobilkt.databinding.MobilItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JalurPemesananAdapter(
    private val context: Context,
    private val data: java.util.ArrayList<Jalur>
) : RecyclerView.Adapter<JalurPemesananAdapter.ViewHolder>() {

    private lateinit var firebaseRef : DatabaseReference

    class ViewHolder(val binding : JalurPemesananItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JalurPemesananAdapter.ViewHolder {
        return ViewHolder(JalurPemesananItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: JalurPemesananAdapter.ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.apply {
            binding.apply {
                dari.text = currentItem.dari
                sampai.text = currentItem.sampai
                supir.text = currentItem.supir
                mobil.text = currentItem.mobil
                tarif.text = currentItem.harga
                wrapper.setOnClickListener {
                    val intent = android.content.Intent(context, PemesananActivity::class.java)
                    intent.putExtra("dataId", currentItem.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}