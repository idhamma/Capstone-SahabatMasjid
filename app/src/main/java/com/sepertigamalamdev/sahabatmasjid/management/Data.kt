package com.sepertigamalamdev.sahabatmasjid.management

data class Barang(
    var id: String = "",
    var masjidid: String = "",
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
    var alamat: String = "",
)


data class Role(
    var id: String = "",
    var uid: String = "",
    var masjidid: String = "",
    var uid_masjidid: String = "",
    var status: String = "",
)

data class RoleRequest(
    var id: String = "",
    var uid: String = "",
    var masjidid: String = "",
    var status: String = "pending", // pending, approved, rejected
    var requestedAt: Long = System.currentTimeMillis() // timestamp pengajuan
)

data class User(
    var id: String = "",
    var name: String = "",
    var nickname: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var address: String = "",
    var photoUrl: String = "",
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
    var jumlah: Int = 0,

    var imageUrlBuktiPinjam: String = "",
    var timestampBuktiPinjam: Long = 0,
    var imageUrlBuktiKembali: String = "",
    var timestampBuktiKembali: Long = 0


)
