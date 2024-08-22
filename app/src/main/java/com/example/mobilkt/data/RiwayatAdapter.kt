package com.example.mobilkt.data

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
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
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.JalurItemBinding
import com.example.mobilkt.databinding.JalurPemesananItemBinding
import com.example.mobilkt.databinding.KotaItemBinding
import com.example.mobilkt.databinding.MobilItemBinding
import com.example.mobilkt.databinding.PemesananItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RiwayatAdapter(
    private val context: Context,
    private val data: java.util.ArrayList<Pemesanan>
) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    private lateinit var firebaseRef : DatabaseReference

    class ViewHolder(val binding : PemesananItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatAdapter.ViewHolder {
        return ViewHolder(PemesananItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RiwayatAdapter.ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.apply {
            binding.apply {
                dari.text = currentItem.dari
                sampai.text = currentItem.sampai
                supir.text = currentItem.supir
                mobil.text = currentItem.mobil
                tarif.text = currentItem.harga.toString()
                tanggal.text = currentItem.tanggal
                nama.text = currentItem.displayName
                hp.text = currentItem.hp
                if (currentItem.status == 0) {
                    status.text = "Belum Dibayar"
                    status.setTextColor(Color.parseColor("#FF0000"))
                }
                if (currentItem.status == 1) {
                    status.text = "Sudah Dibayar"
                    status.setTextColor(Color.parseColor("#0000FF"))
                }
                if (currentItem.status == 2) {
                    status.text = "Dalam Perjalanan"
                    status.setTextColor(Color.parseColor("#FF00FF"))
                }
                if (currentItem.status == 3) {
                    status.text = "Selesai"
                    status.setTextColor(Color.parseColor("#00FF00"))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}