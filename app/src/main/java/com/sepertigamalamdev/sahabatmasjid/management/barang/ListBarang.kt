package com.sepertigamalamdev.sahabatmasjid.management.barang
//

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import coil.compose.SubcomposeAsyncImage

//import com.sepertigamalamdev.sahabatmasjid.Routes
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang

import com.sepertigamalamdev.sahabatmasjid.management.UserViewModel
import com.sepertigamalamdev.sahabatmasjid.profile.InitialAvatar

@Composable
fun ItemListScreen(navController: NavController, borrow: Boolean = false, masjidid: String) {
    val database = FirebaseDatabase.getInstance().getReference("barang")
    val barangListState = remember { mutableStateListOf<Barang>() } // Daftar asli dari Firebase

    var searchQuery by remember { mutableStateOf("") }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    val viewModel: UserViewModel = viewModel()
    var userStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUserRole(uid, masjidid) { status ->
            userStatus = status
        }
    }

    LaunchedEffect(masjidid, borrow) { // Re-fetch jika masjidid atau mode borrow berubah
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                barangListState.clear()
                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(Barang::class.java)

                    if (barang?.masjidid == masjidid) {
                        if (borrow) {
                            if (barang.availability == true && barang.dapatDipinjam == true && barang.stock > 0) {
                                barangListState.add(barang)
                            }
                        } else {
                            barangListState.add(barang)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal ambil data: ${error.message}")
            }
        })
    }

    val displayedList = remember(searchQuery, barangListState.toList()) {
        if (searchQuery.isBlank()) {
            barangListState
        } else {
            barangListState.filter { barang ->
                val nameMatch = barang.name.contains(searchQuery, ignoreCase = true)
                // Pastikan kodeInventaris tidak null jika fieldnya nullable di data class Barang Anda
                val kodeMatch = barang.kodeInventaris?.contains(searchQuery, ignoreCase = true) ?: false
                nameMatch || kodeMatch
            }
        }
    }

    Scaffold(bottomBar = { Footer(navController) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Padding dari Scaffold diterapkan di sini
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    // .padding(innerPadding) // Hapus ini jika sudah diterapkan di Box terluar
                    .padding(horizontal = 16.dp) // Hanya padding horizontal untuk Column konten
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), // Padding vertikal untuk Row header
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "Daftar Barang",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Search Bar Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp), // Sesuaikan padding internal search bar
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // 2. Update BasicTextField
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) { // Box untuk mengatur posisi placeholder & teks
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Cari nama barang atau kode", // Placeholder yang lebih jelas
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField() // Tempat teks input pengguna akan muncul
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Spasi setelah search bar

                //Update LazyColumn untuk menggunakan displayedList
                if (displayedList.isEmpty() && searchQuery.isNotBlank()) {
                    Text(
                        text = "Tidak ada barang yang cocok dengan pencarian \"$searchQuery\".",
                        modifier = Modifier.padding(top = 20.dp).align(Alignment.CenterHorizontally),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                } else if (displayedList.isEmpty() && barangListState.isNotEmpty()){
                    // Kasus ini seharusnya tidak terjadi jika filter benar, kecuali barangListState awalnya kosong
                }
                else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp), // Beri jarak antar card
                        contentPadding = PaddingValues(bottom = 16.dp) // Padding di bawah list
                    ) {
                        items(items = displayedList, key = { barang -> barang.id }) { barang -> // Gunakan key untuk performa
                            BarangCard(barang, navController)
                        }
                    }
                }
            }

            if (userStatus == "operator") {
                FloatingActionButton(
                    onClick = { navController.navigate("tambahBarang/$masjidid") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp), // Padding FAB dari tepi Box
                    containerColor = Color(0xFF34A853),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp) // Bentuk FAB bisa disesuaikan
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Jika Card dari M3
@Composable
fun BarangCard(barang: Barang, navController: NavController) {
    val id = barang.id
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (id.isNotBlank()) {
                    navController.navigate("detailBarang/${barang.masjidid}/$id")
                } else {
                    Log.e("NAVIGATION", "Barang ID is null or blank, cannot navigate to detailBarang.")
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Tampilan Gambar Barang atau Inisial ---
            Box(
                modifier = Modifier
                    .size(64.dp) // Ukuran untuk gambar barang
                    .clip(RoundedCornerShape(8.dp)) // Bentuk persegi tumpul
                    .background(MaterialTheme.colorScheme.surfaceVariant), // Warna latar jika gambar gagal
                contentAlignment = Alignment.Center
            ) {
                if (barang.imageUrl.isNotBlank()) {

                    ProductImage(barang.imageUrl,barang.name)
                } else {
                    // Tampilkan inisial jika tidak ada imageUrl
                    Text(
                        text = barang.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Sesuaikan warna jika perlu
                    )
                }
            }
            // --- Akhir Tampilan Gambar Barang ---

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = barang.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (barang.kodeInventaris?.isNotBlank() == true) {
                    Text(
                        text = "Kode: ${barang.kodeInventaris}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = "Stok: ${barang.stock}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}


@Composable
fun ProductImage(
    imageUrl: String,
    productName: String,
    modifier: Modifier = Modifier // Modifier untuk Box luar, memungkinkan kustomisasi ukuran & bentuk dari pemanggil
) {
    Box(
        modifier = modifier // Modifier dari parameter diterapkan di sini
            .background(MaterialTheme.colorScheme.surfaceVariant), // Warna latar belakang default jika gambar transparan atau gagal total
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = imageUrl,
//                key = imageUrl, // Penting untuk refresh jika URL (dengan cache buster) berubah
                contentDescription = "Gambar $productName",
                modifier = Modifier.fillMaxSize(), // Mengisi Box
                contentScale = ContentScale.Crop, // Atau .Fit, atau .Inside sesuai kebutuhan
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp) // Ukuran progress indicator bisa lebih kecil
                            .align(Alignment.Center)
                    )
                },
                error = { // Jika gagal load gambar, tampilkan inisial produk
                    InitialAvatar(name = productName)
                }
            )
        } else {
            // Tampilkan inisial jika tidak ada imageUrl
            InitialAvatar(name = productName)
        }
    }
}