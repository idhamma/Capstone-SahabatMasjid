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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.sepertigamalamdev.sahabatmasjid.management.ManageBorrowingsTabContent
import com.sepertigamalamdev.sahabatmasjid.management.Peminjaman
import com.sepertigamalamdev.sahabatmasjid.management.PeminjamanViewModel
import com.sepertigamalamdev.sahabatmasjid.management.UserViewModel
import java.util.Locale

@Composable
fun PeminjamanBarang(navController: NavController, viewModel: PeminjamanViewModel = viewModel()) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val originalPeminjamanBarangList by viewModel.peminjamanBarangList.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        uid?.let { viewModel.loadPeminjamanBarang(it) }
    }

    val displayedList = remember(searchQuery, originalPeminjamanBarangList) {
        if (searchQuery.isBlank()) {
            originalPeminjamanBarangList
        } else {
            originalPeminjamanBarangList.filter { (peminjaman, barang) -> // Destrukturisasi Pair
                val nameMatch = barang.name.contains(searchQuery, ignoreCase = true)
                val kodeMatch = barang.kodeInventaris?.contains(searchQuery, ignoreCase = true) ?: false
                val statusMatch = peminjaman.status.contains(searchQuery, ignoreCase = true)
                nameMatch || kodeMatch || statusMatch
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header "Daftar Peminjaman"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Daftar Peminjaman Saya",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // UI Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari nama barang, kode, atau status",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Tampilkan daftar atau pesan jika kosong
        if (displayedList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (searchQuery.isBlank() && originalPeminjamanBarangList.isEmpty()) {
                    Text(
                        "Anda belum memiliki peminjaman barang.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else if (searchQuery.isNotBlank()) {
                    Text(
                        "Tidak ada peminjaman yang cocok dengan \"$searchQuery\".",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        "Tidak ada data peminjaman untuk ditampilkan.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items = displayedList, key = { (peminjaman, _) -> peminjaman.id }) { (peminjaman, barang) ->
                    BarangCardPeminjaman(peminjaman, barang, navController)
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
    val id = peminjaman.id

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (id.isNotBlank()) {
                    navController.navigate("detailPeminjaman/$id")
                } else {
                    Log.e("NAVIGATION", "Peminjaman ID is null or blank")
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = barang.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
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
                    text = "Status: ${peminjaman.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when (peminjaman.status.lowercase(Locale.getDefault())) {
                        "diajukan" -> Color(0xFFFFA000) // Orange (Amber 700)
                        "disetujui" -> Color(0xFF1976D2) // Blue (Blue 700)
                        "dipinjam" -> Color(0xFFD32F2F)  // Red (Red 700)
                        "dikembalikan" -> Color(0xFF388E3C) // Green (Green 700)
                        "ditolak" -> MaterialTheme.colorScheme.error // Warna error dari tema
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
                        if (id.isNotBlank()) {
                            navController.navigate("detailPeminjaman/$id")
                        } else {
                            Log.e("NAVIGATION", "Peminjaman ID is null or blank on detail click")
                        }
                    }
            )
        }
    }
}

// Composable PeminjamanScreen (tetap sama)
// Di PeminjamanScreen.kt (menggunakan UserViewModel dan PeminjamanViewModel)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeminjamanScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    peminjamanViewModel: PeminjamanViewModel = viewModel()
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    // Mengambil daftar masjid yang dikelola dari UserViewModel
    val managedMasjidsInfo by userViewModel.managedMasjidsInfo.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Memuat peran manajemen pengguna saat layar pertama kali dibuat atau uid berubah
    LaunchedEffect(uid) {
        uid?.let {
            userViewModel.loadUserManagementRoles(it)
        }
    }

    val tabs = remember(managedMasjidsInfo) { // Daftar tab bergantung pada apakah pengguna adalah operator
        mutableListOf("Peminjaman Saya").apply {
            if (managedMasjidsInfo.isNotEmpty()) { // Jika pengguna mengelola masjid, tambahkan tab "Kelola"
                add("Kelola Peminjaman")
            }
        }
    }

    Scaffold(
        bottomBar = { Footer(navController = navController) } // Footer Anda akan tetap ada di sini
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (tabs.size > 1) { // Tampilkan TabRow hanya jika ada lebih dari satu tab
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }
            } else if (tabs.isEmpty() && uid != null) {
                // Mungkin terjadi jika loadUserManagementRoles belum selesai & managedMasjidsInfo masih kosong
                // Anda bisa tampilkan loading atau biarkan konten tab default (Peminjaman Saya)
            }


            // Konten berdasarkan tab yang dipilih
            when (selectedTabIndex) {
                0 -> {
                    // Menampilkan daftar peminjaman pribadi pengguna
                    PeminjamanBarang(navController = navController, viewModel = peminjamanViewModel)
                }
                1 -> {
                    // Ini adalah tab "Kelola Peminjaman", hanya ada jika pengguna adalah operator
                    if (managedMasjidsInfo.isNotEmpty()) {
                        ManageBorrowingsTabContent(
                            navController = navController,
                            managedMasjids = managedMasjidsInfo,
                            peminjamanViewModel = peminjamanViewModel
                        )
                    }
                }
            }
        }
    }
}


//untuk operator

@Composable
fun OperatorMasjidPeminjamanList(
    navController: NavController,
    masjidId: String,
    masjidName: String,
    viewModel: PeminjamanViewModel = viewModel() // Gunakan instance ViewModel yang sama jika logis
) {
    // Ambil daftar peminjaman yang perlu dikelola dari ViewModel
    val peminjamanUntukPengelolaanList by viewModel.peminjamanUntukPengelolaan.collectAsState()

    // Panggil fungsi untuk memuat data saat masjidId berubah
    LaunchedEffect(masjidId) {
        viewModel.loadPeminjamanUntukPengelolaan(masjidId, statusFilter = "diajukan") // Atau status lain yang relevan
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp) // Padding konsisten
    ) {
        Text(
            "Kelola Peminjaman: $masjidName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            "Status Diajukan", // Anda bisa membuat ini dinamis jika ada filter status lain
            style = MaterialTheme.typography.titleSmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (peminjamanUntukPengelolaanList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tidak ada peminjaman yang perlu dikelola untuk masjid ini saat ini.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items = peminjamanUntukPengelolaanList,
                    key = { (peminjaman, _) -> peminjaman.id } // Key untuk performa
                ) { (peminjaman, barang) ->
                    OperatorPeminjamanItemCard(
                        peminjaman = peminjaman,
                        barang = barang,
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Jika menggunakan Card dari M3
@Composable
fun OperatorPeminjamanItemCard(
    peminjaman: Peminjaman,
    barang: Barang,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Navigasi ke layar detail operator dengan ID peminjaman
                navController.navigate("operator_detail_peminjaman/${peminjaman.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp), // Bentuk card
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Agar ikon panah di ujung kanan
        ) {
            Column(modifier = Modifier.weight(1f)) { // Kolom teks mengambil sisa ruang
                Text(
                    text = barang.name, // Nama barang yang dipinjam
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium // Ukuran teks nama barang
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Peminjam: ${peminjaman.namaPeminjam}", // Nama peminjam
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Jumlah: ${peminjaman.jumlah}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Tgl Diajukan: ${peminjaman.tanggalPengajuan}", // Tanggal pengajuan dari objek Peminjaman
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Status: ${peminjaman.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = when (peminjaman.status.lowercase(Locale.getDefault())) {
                        "diajukan" -> Color(0xFFFFA000) // Oranye untuk "diajukan"
                        // Tambahkan warna untuk status lain jika daftar ini bisa menampilkan status lain
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Lihat Detail Peminjaman",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}