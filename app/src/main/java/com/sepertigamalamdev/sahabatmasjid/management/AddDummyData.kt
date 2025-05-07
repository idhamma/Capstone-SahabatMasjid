package com.sepertigamalamdev.sahabatmasjid.management

import com.google.firebase.database.FirebaseDatabase



fun simpanBarangDummy() {
    // Dummy data
    val dummyMasjid = Masjid(
        name = "Masjid Al-Falah",
        alamat = "Jl. Merdeka No. 10, Jakarta"
    )

    val dummyBarangList = listOf(
        Barang(
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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
            masjid = dummyMasjid,
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

//
//    val dummyEmailOperator = emailOperator(
//        masjid = dummyMasjid,
//        email = "operator@alfalah.id"
//    )
//
//    val dummyEmailJemaah = emailJemaah(
//        masjid = dummyMasjid,
//        email = "jemaah1@alfalah.id"
//    )

    val database = FirebaseDatabase.getInstance()
//
//    // Simpan barang
//    val refMasjid = database.getReference("barang").push()
//    val refBarang = database.getReference("barang")
//    dummyBarangList.forEach { barang ->
//        val key = refBarang.push().key
//        key?.let {
//            val barangWithId = barang.copy(id = it) // sisipkan id
//            refBarang.child(it).setValue(barangWithId)
//        }
//    }

    // Simpan masjid
    val refMasjid = database.getReference("masjid").push()
    refMasjid.setValue(dummyMasjid)
//
//    // Simpan email operator
//    val refEmailOp = database.getReference("emailOperator").push()
//    refEmailOp.setValue(dummyEmailOperator)
//
//    // Simpan email jemaah
//    val refEmailJm = database.getReference("emailJemaah").push()
//    refEmailJm.setValue(dummyEmailJemaah)
}