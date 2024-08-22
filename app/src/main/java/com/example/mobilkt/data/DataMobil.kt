package com.example.mobilkt.data

import com.example.mobilkt.data.model.Mobil

val nama = arrayOf("Avanza", "Inova")

val listData: ArrayList<Mobil>
    get() {
        val list = arrayListOf<Mobil>()
        for (position in nama.indices) {
            val mobil = Mobil()
            mobil.nama = nama[position]
            list.add(mobil)
        }
        return list
    }