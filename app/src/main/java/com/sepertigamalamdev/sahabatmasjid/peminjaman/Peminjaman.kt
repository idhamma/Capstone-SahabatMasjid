package com.sepertigamalamdev.sahabatmasjid.peminjaman


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang
import com.sepertigamalamdev.sahabatmasjid.management.Peminjaman

@Composable
fun PeminjamanBarang(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val databasePeminjaman = FirebaseDatabase.getInstance().getReference("peminjaman")
    val databaseBarang = FirebaseDatabase.getInstance().getReference("barang")

    val pinjamanList = remember { mutableStateListOf<Peminjaman>() }
    val barangList = remember { mutableStateListOf<Barang>() }

    val peminjamanBarangList = remember { mutableStateListOf<Pair<Peminjaman, Barang>>() }


    LaunchedEffect(uid, pinjamanList, barangList) {
        uid?.let {
            databasePeminjaman.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pinjamanList.clear()
                    barangList.clear()

                    val tempPeminjamanList = mutableListOf<Peminjaman>()
                    val tempBarangList = mutableListOf<Pair<Peminjaman, Barang>>()

                    for (pinjamanSnapshot in snapshot.children) {
                        try {
                            if (pinjamanSnapshot.value is Map<*, *>) {
                                val peminjaman = pinjamanSnapshot.getValue(Peminjaman::class.java)
                                val peminjamanUid = pinjamanSnapshot.child("uid").getValue(String::class.java)

                                if (peminjaman != null && peminjamanUid == uid) {
                                    tempPeminjamanList.add(peminjaman)
                                }
                            } else {
                                Log.w("Firebase", "Node peminjaman bukan objek, dilewati: ${pinjamanSnapshot.key}")
                            }
                        } catch (e: Exception) {
                            Log.e("Firebase", "Gagal parsing peminjaman: ${e.message}")
                        }
                    }

                    if (tempPeminjamanList.isEmpty()) {
                        Log.d("Firebase", "Tidak ada peminjaman untuk UID ini.")
                        return
                    }

                    var counter = 0
                    val total = tempPeminjamanList.size

                    for (peminjaman in tempPeminjamanList) {
                        val barangId = peminjaman.barangid
                        databaseBarang.child(barangId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(barangSnapshot: DataSnapshot) {
                                val barang = barangSnapshot.getValue(Barang::class.java)
                                if (barang != null) {
                                    tempBarangList.add(peminjaman to barang)
                                }

                                counter++
                                if (counter == total) {
                                    pinjamanList.clear()
                                    barangList.clear()
                                    peminjamanBarangList.clear()

                                    pinjamanList.addAll(tempPeminjamanList)
                                    peminjamanBarangList.addAll(tempBarangList)
                                    Log.d("Firebase", "Gabungan selesai: ${peminjamanBarangList.size} item")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Firebase", "Gagal ambil barang: ${error.message}")
                                counter++
                                if (counter == total) {
                                    pinjamanList.clear()
                                    barangList.clear()
                                    peminjamanBarangList.clear()

                                    pinjamanList.addAll(tempPeminjamanList)
                                    peminjamanBarangList.addAll(tempBarangList)
                                }
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Gagal ambil data peminjaman: ${error.message}")
                }
            })
        }
    }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Daftar Peminjaman",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            when {
                peminjamanBarangList.isEmpty() -> {
                    Text("Belum ada peminjaman")
                }
                else -> {
                    LazyColumn {
                        items(peminjamanBarangList) { (peminjaman, barang) ->
                            BarangCardPeminjaman(peminjaman, barang, navController)
                        }
                    }
                }
            }


        }
}
@Composable
fun BarangCardPeminjaman(
    peminjaman: Peminjaman,
    barang: Barang,
    navController: NavController
) {
    var id = peminjaman.id
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (!id.isNullOrBlank()) {
                    navController.navigate("detailPeminjaman/$id")
                } else {
                    Log.e("NAVIGATION", "Barang ID is null or blank")
                    navController.navigate("homepage")
                }
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Inisial nama barang
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = barang.name.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = barang.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Kode Inventaris: ${barang.kodeInventaris}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "peminjaman id: ${peminjaman.id}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Status: ${peminjaman.status}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when (peminjaman.status.lowercase()) {
                        "dipinjam" -> Color(0xFFEF6C00) // orange
                        "dikembalikan" -> Color(0xFF388E3C) // green
                        else -> Color.Gray
                    }
                )
            }

            Text(
                text = "Detail",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        if (!id.isNullOrBlank()) {
                            navController.navigate("detailPeminjaman/$id")
                        } else {
                            navController.navigate("homepage")
                        }
                    }
            )
        }
    }
}

@Composable
fun PeminjamanScreen(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
        ) {
            PeminjamanBarang(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PeminjamanScreenPreview(navController: NavHostController = rememberNavController()){
    PeminjamanScreen(navController = navController)
//    PeminjamanBarang()
}