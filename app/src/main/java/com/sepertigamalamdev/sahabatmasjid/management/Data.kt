package com.sepertigamalamdev.sahabatmasjid.management

data class Barang(
    var id: String = "",
    var masjid: Masjid = Masjid(),
    var name: String = "",
    var kodeInventaris: String = "",
    var stock: Int = 0,
    var total: Int = 0,
    var kondisi: Int = 0,
    var place: String = "",
    var availability: Boolean = false, //availability = true if stock > 0 && dapatDipinjam == true
    var dapatDipinjam: Boolean = false,
    var imageUrl: String = "",
)



data class Masjid(
    val id: String = "",
    val name: String = "",
    val alamat: String = "",
    val operatorUids: List<String> = emptyList(),
    val jemaahUids: List<String> = emptyList(),
    val barang: List<Barang> = emptyList()
)


data class emailOperator(
    var masjid: Masjid = Masjid(),
    var email: String = ""
)

data class emailJemaah(
    var masjid: Masjid = Masjid(),
    var email: String = ""
)

data class user(
    var id: String = "",
    var name: String = "",
    var nickname: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var address: String = "",
    var photoUrl: String = "",
    var role: String = "",
    var timestamp: Long = 0,
)

data class Peminjaman(
    var id: String = "",
    var uid: String = "",
    var barangid: String = "",
    var emailPeminjam: String = "",
    var namaPeminjam: String = "",
    var phoneNumberPeminjam: String = "",
    var alamatPeminjam: String = "",
    var status: String = "",
    var tanggalPengajuan: String = "",
    var tanggalPinjam: String = "",
    var tanggalPengembalian: String = "",
    var timestamp: Long = 0,

    )



