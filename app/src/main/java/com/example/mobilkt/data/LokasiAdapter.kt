package com.example.mobilkt.data

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.example.mobilkt.R
import com.example.mobilkt.data.model.Jalur
import com.example.mobilkt.data.model.Kota
import com.example.mobilkt.data.model.Mobil
import com.example.mobilkt.data.model.Pemesanan
import com.example.mobilkt.databinding.JalurItemBinding
import com.example.mobilkt.databinding.KotaItemBinding
import com.example.mobilkt.databinding.MobilItemBinding
import com.example.mobilkt.databinding.SimpleBetweenItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LokasiAdapter(
    private val context: Context,
    private val data: java.util.ArrayList<Pemesanan>
) : RecyclerView.Adapter<LokasiAdapter.ViewHolder>() {

    private lateinit var firebaseRef : DatabaseReference

    class ViewHolder(val binding : SimpleBetweenItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokasiAdapter.ViewHolder {
        return ViewHolder(SimpleBetweenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LokasiAdapter.ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.apply {
            binding.apply {
                left.text = currentItem.displayName
                right.text = currentItem.dari

                wrapper.setOnClickListener{
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${currentItem.latitude},${currentItem.longitude}?q=${currentItem.latitude},${currentItem.longitude}"))
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}