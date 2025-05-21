package com.sepertigamalamdev.sahabatmasjid.management.barang

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer


import com.sepertigamalamdev.sahabatmasjid.management.Barang
@Composable
fun DetailBarangScreen(navController: NavController, itemId: String) {

    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("barang")
    var barang by remember { mutableStateOf<Barang?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Ambil data berdasarkan ID
    LaunchedEffect(itemId) {
        database.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                barang = snapshot.getValue(Barang::class.java)
                isLoading = false // Set loading to false after fetching data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal ambil data: ${error.message}")
                isLoading = false // Set loading to false even in case of error
            }
        })
    }

    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
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
                    text = "Pinjam Barang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Show Loading Indicator while fetching data
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            barang?.let { item ->
                // Show data only after it is fetched
                DetailBarang(item)

                if (item.availability == true) {
                    Spacer(modifier = Modifier.height(16.dp))

                    if (item.stock > 0) {
                        androidx.compose.material3.Text(
                            text = "Ingin pinjam barang ini?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        androidx.compose.material3.Text(
                            text = "Silahkan klik tombol dibawah ini",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Button(
                            onClick = { navController.navigate("pengajuanPeminjaman/${itemId}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text(
                                text = "Ajukan Peminjaman",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        androidx.compose.material3.Text(
                            text = "Saat ini seluruh barang sedang dipinjam " +
                                    "\nSilahkan cek kembali nanti",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            } ?: run {
                Text("Memuat data barang... ", modifier = Modifier.padding(top = 16.dp))
            }
        }
            barang?.let { item ->
                FloatingActionButton(
                    onClick = { navController.navigate("editBarang/${item.id}") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = Color(0xFF34A853),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create, //sementara
                        contentDescription = "Add Item"
                    )
                }
            }
        }
    }
}

@Composable
fun DetailBarang(item : Barang){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikon barang
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.name.take(1).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Detail informasi barang
            Column {
                Text(text = item.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(text = "Kode Inventaris: ${item.kodeInventaris}", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Jumlah Tersedia: ${item.stock} unit dari ${item.total} total", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Kondisi Barang: ${kondisiText(item.kondisi)}", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Lokasi Penyimpanan: ${item.place}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}


// Fungsi bantu konversi kondisi
fun kondisiText(nilai: Int): String {
    return when {
        nilai >= 8 -> "Sangat Baik"
        nilai >= 5 -> "Baik"
        nilai >= 3 -> "Cukup"
        else -> "Buruk"
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DetailBarangScreenPreview(navController: NavHostController = rememberNavController()) {
//    DetailBarangScreen(navController = navController, itemId = itemId)
//}