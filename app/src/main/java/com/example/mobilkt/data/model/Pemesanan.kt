package com.example.mobilkt.data.model

data class Pemesanan (
    val id: String? = null,
    var idUser: String = "",
    var displayName: String = "",
    var email: String = "",
    var hp: String = "",
    var harga: Int = 0,
    var idJalur: String = "",
    var mobil: String = "",
    var supirId: String = "",
    var supir: String = "",
    var dari: String = "",
    var sampai: String = "",
    var tanggal: String = "",
    var status: Int? = 0,
)