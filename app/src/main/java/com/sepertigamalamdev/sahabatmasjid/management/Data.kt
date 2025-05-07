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
    var id: String = "",
    var name: String = "",
    var alamat: String = ""
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
    var name: String = "",
    var email: String = "",
    var password: String = ""
)

data class peminjaman(
    var masjid: Masjid = Masjid(),
    var barang: Barang,
    var tanggalPinjam: String = "",
    var tanggalKembali: String = "",
    var peminjam: user,
)