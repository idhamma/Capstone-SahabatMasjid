package com.sepertigamalamdev.sahabatmasjid.management

import com.google.firebase.database.FirebaseDatabase



fun simpanBarangDummy() {

    val dummyBarangList = listOf(
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-002",
            name = "Kipas Angin",
            stock = 3,
            total = 5,
            kondisi = 2,
            place = "Serambi Masjid",
            dapatDipinjam = false,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-003",
            name = "Karpet",
            stock = 10,
            total = 10,
            kondisi = 9,
            place = "Ruang Utama",
            dapatDipinjam = false,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-004",
            name = "Proyektor",
            stock = 2,
            total = 2,
            kondisi = 7,
            place = "Gudang",
            dapatDipinjam = true,
            availability = false,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-005",
            name = "Pengeras Suara",
            stock = 4,
            total = 6,
            kondisi = 5,
            place = "Menara Masjid",
            dapatDipinjam = true,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-006",
            name = "Meja Lipat",
            stock = 8,
            total = 8,
            kondisi = 10,
            place = "Gudang",
            dapatDipinjam = true,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-007",
            name = "Kursi Lipat",
            stock = 15,
            total = 20,
            kondisi = 8,
            place = "Gudang",
            dapatDipinjam = true,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-008",
            name = "Mimbar",
            stock = 1,
            total = 1,
            kondisi = 9,
            place = "Depan Jamaah",
            dapatDipinjam = false,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-009",
            name = "Al-Qur'an",
            stock = 30,
            total = 30,
            kondisi = 10,
            place = "Rak Kitab",
            dapatDipinjam = true,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-010",
            name = "Lampu LED",
            stock = 20,
            total = 25,
            kondisi = 6,
            place = "Langit-langit Masjid",
            dapatDipinjam = false,
            availability = true,
            imageUrl = ""
        ),
        Barang(
            masjidid = "-OQXit3aG2CJnROTRc4A",
            kodeInventaris = "INV-011",
            name = "Tikar",
            stock = 6,
            total = 6,
            kondisi = 7,
            place = "Ruang Belakang",
            dapatDipinjam = true,
            availability = true,
            imageUrl = ""
        )
    )

    val database = FirebaseDatabase.getInstance()
//
    // Simpan barang
//    val refBarang = database.getReference("barang")
//    dummyBarangList.forEach { barang ->
//        val key = refBarang.push().key
//        key?.let {
//            val barangWithId = barang.copy(id = it) // sisipkan id
//            refBarang.child(it).setValue(barangWithId)
//        }
//    }

    val datarole = listOf(
        Role(
            uid = "5Yjy3McbxNdsEfcKqarhwB2c42l1",
            masjidid = "-OQXit3aG2CJnROTRc4A",
            status = "jemaah"
        ),
        Role(
            uid = "P9qJXntiE5gTazvBTHeqRc6NUk42",
            masjidid = "-OQXit3aG2CJnROTRc4A",
            status = "operator"
        ),
    )

    val ref = FirebaseDatabase.getInstance().getReference("role")
    datarole.forEach { role ->
        val key = ref.push().key
        key?.let {
            val role = Role(
                id = it,
                uid = role.uid,
                masjidid = role.masjidid,
                status = role.status
            )
            val roleMap = role.copy(
                id = it,
                uid_masjidid = "${role.uid}-${role.masjidid}"
            )
            ref.child(it).setValue(roleMap)
        }
    }


//    val refRole = database.getReference("role")
//    datarole.forEach { role ->
//        val key = refRole.push().key
//        key?.let {
//            val roleWithId = role.copy(id = it) // sisipkan id
//            refRole.child(it).setValue(roleWithId)
//        }
//    }


}

fun tambahMasjid(nama: String, alamat: String, onResult: (Boolean, String?) -> Unit) {
    val database = FirebaseDatabase.getInstance().getReference("masjid")
    val id = database.push().key // Buat ID unik secara otomatis

    if (id != null) {
        val masjid = Masjid(id = id, name = nama, alamat = alamat)

        database.child(id).setValue(masjid)
            .addOnSuccessListener {
                onResult(true, null) // Berhasil
            }
            .addOnFailureListener { error ->
                onResult(false, error.message) // Gagal
            }
    } else {
        onResult(false, "Gagal membuat ID masjid")
    }
}
