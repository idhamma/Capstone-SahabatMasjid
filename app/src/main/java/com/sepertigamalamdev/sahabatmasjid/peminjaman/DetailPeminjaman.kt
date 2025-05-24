package com.sepertigamalamdev.sahabatmasjid.peminjaman


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang
import com.sepertigamalamdev.sahabatmasjid.management.Peminjaman
import androidx.navigation.compose.rememberNavController
import com.sepertigamalamdev.sahabatmasjid.management.BarangViewModel
import com.sepertigamalamdev.sahabatmasjid.management.PeminjamanViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel

fun formatMillisToDateTimeString(
    millis: Long,
    pattern: String = "dd MMMM yyyy, HH:mm", // Contoh pola: "14 Maret 2025, 09:39"
    locale: Locale = Locale("id", "ID") // Menggunakan Locale Indonesia
): String {
    if (millis == 0L) return "N/A" // Atau string default lain jika timestamp 0
    return try {
        val instant = Instant.ofEpochMilli(millis)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        localDateTime.format(formatter)
    } catch (e: Exception) {
        Log.e("FormatDateTime", "Error formatting timestamp: $millis", e)
        "Invalid Date" // Fallback jika ada error
    }
}

@Composable
fun BorrowDetailContent(
    navController: NavController,
    peminjaman: Peminjaman,
    barang: Barang?,
    isBarangLoadingFailed: Boolean // true jika barangID ada, tapi barang gagal dimuat/tidak ditemukan
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Tambahkan scroll jika konten panjang
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF34A853))
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Detail Peminjaman",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Waktu Pengajuan
        Text(
            // Menampilkan timestamp yang diformat DAN string tanggalPengajuan asli
            text = "Waktu Pengajuan: ${formatMillisToDateTimeString(peminjaman.timestamp)} (${peminjaman.tanggalPengajuan})",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Item Icon and Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, // Placeholder
                contentDescription = "Item Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }

        if (barang != null) {
            Text(
                text = barang.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Kode Inventaris: ${barang.kodeInventaris}",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Jumlah Tersedia: ${barang.stock} unit dari ${barang.total} total",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Kondisi Barang: ${barang.kondisi}",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else if (peminjaman.barangid.isNotBlank()) {
            if (isBarangLoadingFailed) {
                Text(
                    text = "Detail barang tidak ditemukan.",
                    fontSize = 14.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            } else {
                // Barang sedang dimuat atau barangid tidak valid tapi belum gagal secara eksplisit
                Text(
                    text = "Memuat detail barang...",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical=8.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields (Read-Only)
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(value = peminjaman.namaPeminjam, onValueChange = {}, label = { Text("Nama Peminjam") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), enabled = false, colors = textFieldColors)
        OutlinedTextField(value = peminjaman.phoneNumberPeminjam, onValueChange = {}, label = { Text("Nomor Telepon") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), enabled = false, colors = textFieldColors)
        OutlinedTextField(value = peminjaman.jumlah.toString(), onValueChange = {}, label = { Text("Jumlah Barang Dipinjam") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), enabled = false, colors = textFieldColors)

        // Untuk tanggalPinjam dan tanggalPengembalian, jika mereka adalah Long timestamp yang perlu diformat:
        // OutlinedTextField(value = formatMillisToDateTimeString(peminjaman.tanggalPinjam), ...)
        // Jika sudah String yang benar:
        OutlinedTextField(value = peminjaman.tanggalPinjam, onValueChange = {}, label = { Text("Tanggal Peminjaman") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), enabled = false, colors = textFieldColors)
        OutlinedTextField(value = peminjaman.tanggalPengembalian, onValueChange = {}, label = { Text("Tanggal Pengembalian") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), enabled = false, colors = textFieldColors)

        if(peminjaman.status == "disetujui"){
            Spacer(modifier = Modifier.weight(1f))

            // Tombol Ajukan Peminjaman
            Button(
                onClick = {}
                ,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
            ) {
                Text(text = "Ajukan Pengembalian", fontSize = 16.sp, color = Color.White)
            }
        }


    }
}

@Composable
fun DetailBorrowScreen(
    navController: NavController,
    borrowId: String,
    peminjamanViewModel: PeminjamanViewModel = viewModel(),
    barangViewModel: BarangViewModel = viewModel()
) {
    val peminjamanFromVm = peminjamanViewModel.peminjaman.value
    val barangFromVm = barangViewModel.barang.value

    // State untuk melacak status loading data
    var isPeminjamanLoading by remember { mutableStateOf(true) }
    var isBarangLoading by remember { mutableStateOf(false) } // Awalnya false, jadi true saat barangid ada
    var barangLoadFailed by remember { mutableStateOf(false) }


    // Ambil data peminjaman berdasarkan ID
    LaunchedEffect(borrowId) {
        isPeminjamanLoading = false
        barangLoadFailed = false // Reset status kegagalan barang
        // Modifikasi ViewModel Anda untuk memberi tahu kapan pengambilan data selesai
        // Ini contoh jika ViewModel memiliki callback atau mengembalikan status
        peminjamanViewModel.getPeminjamanById(borrowId)
//        if(peminjamanFromVm == null){
//            isPeminjamanLoading = true
//        }
    }

        LaunchedEffect(peminjamanFromVm?.barangid) {
            peminjamanFromVm?.let {
            barangViewModel.getBarangById(it.barangid)
            isBarangLoading = true
        }
    }

    Scaffold(
        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isPeminjamanLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (peminjamanFromVm == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Data peminjaman tidak ditemukan.")
                }
            } else {
                // peminjamanFromVm dijamin non-null di sini
                BorrowDetailContent(
                    navController = navController,
                    peminjaman = peminjamanFromVm,
                    barang = barangFromVm,
                    // Jika barang sedang loading DAN barangid ada, atau jika gagal memuat barang
                    isBarangLoadingFailed = (peminjamanFromVm.barangid.isNotBlank() && barangFromVm == null && barangLoadFailed)
                )
            }
        }
    }
}


//PENTING JANGAN DIHAPUS
//@Composable
//fun DetailBorrowScreen(
//    navController: NavController,
//    borrowId: String,
//    peminjamanViewModel: PeminjamanViewModel = viewModel(),
//    barangViewModel: BarangViewModel = viewModel()
//) {
//    val peminjaman = peminjamanViewModel.peminjaman.value
//    val barang = barangViewModel.barang.value
//
//    var isLoadingPeminjaman by remember { mutableStateOf(true) }
//    var isLoadingBarang by remember { mutableStateOf(true) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    // Ambil data peminjaman berdasarkan ID
//    LaunchedEffect(borrowId) {
//        peminjamanViewModel.getPeminjamanById(borrowId)
//    }
//
//    val currentPeminjaman = peminjaman
//
//    // Ketika data peminjaman sudah ada, ambil data barang
//    LaunchedEffect(peminjaman) {
//        peminjaman?.let {
//            barangViewModel.getBarangById(it.barangid)
//            isLoading = false
//        }
//    }
//
//    Scaffold(
//        bottomBar = { Footer(navController = navController) }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            if (isLoading) {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            } else if (peminjaman == null) {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text("Data peminjaman tidak ditemukan.")
//                }
//            } else {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                        .padding(16.dp)
//                ) {
//                    // Header
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(Color(0xFF34A853)) // Warna hijau dari UI "mentah"
//                            .padding(vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(
//                                imageVector = Icons.Default.ArrowBack,
//                                contentDescription = "Back",
//                                tint = Color.White
//                            )
//                        }
//                        Text(
//                            text = "Detail Peminjaman", // Judul disesuaikan
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White,
//                            modifier = Modifier.padding(start = 8.dp)
//                        )
//                    }
//
//                    // Waktu Pengajuan
//                    Text(
//                        text = "Waktu Pengajuan: ${formatMillisToDateTimeString(currentPeminjaman.timestamp)} ${currentPeminjaman.tanggalPengajuan}", // Data dinamis
//                        fontSize = 14.sp,
//                        color = Color.Gray,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Item Icon and Details
//                    // Jika ada URL gambar, Anda bisa menggunakan AsyncImage dari Coil di sini
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack, // Placeholder, ganti dengan ikon barang jika ada
//                            contentDescription = "Item Icon",
//                            tint = Color.Gray,
//                            modifier = Modifier
//                                .size(80.dp) // Ukuran ikon bisa disesuaikan
//                                .background(Color.LightGray, RoundedCornerShape(8.dp))
//                                .padding(16.dp)
//                        )
//                    }
//
//                    if (barang != null) {
//                        Text(
//                            text = barang.name, // Data dinamis
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Text(
//                            text = "Kode Inventaris: ${barang.kodeInventaris}", // Data dinamis
//                            fontSize = 14.sp,
//                            color = Color.Gray,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Text(
//                            text = "Jumlah Tersedia: ${barang.stock} unit dari ${barang.total} total", // Data dinamis
//                            fontSize = 14.sp,
//                            color = Color.Gray,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Text(
//                            text = "Kondisi Barang: ${barang.kondisi}", // Data dinamis
//                            fontSize = 14.sp,
//                            color = Color.Gray,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    } else if (peminjaman.barangid.isNotBlank() && !isLoadingBarang) {
//                        Text(
//                            text = "Detail barang tidak ditemukan.",
//                            fontSize = 14.sp,
//                            color = Color.Red,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
//                        )
//                    }
//
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Form Fields (Read-Only)
//                    OutlinedTextField(
//                        value = currentPeminjaman.namaPeminjam,
//                        onValueChange = { /* Read-only */ },
//                        label = { Text("Nama Peminjam") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        enabled = false,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            // Kustomisasi warna untuk read-only
//                            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
//                            disabledBorderColor = MaterialTheme.colorScheme.outline,
//                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    )
//
//                    OutlinedTextField(
//                        value = currentPeminjaman.phoneNumberPeminjam,
//                        onValueChange = { /* Read-only */ },
//                        label = { Text("Nomor Telepon") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        enabled = false,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
//                            disabledBorderColor = MaterialTheme.colorScheme.outline,
//                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    )
//
//                    OutlinedTextField(
//                        value = currentPeminjaman.jumlah.toString(), // Asumsi ada field jumlahDipinjam
//                        onValueChange = { /* Read-only */ },
//                        label = { Text("Jumlah Barang Dipinjam") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        enabled = false,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
//                            disabledBorderColor = MaterialTheme.colorScheme.outline,
//                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    )
//
//                    OutlinedTextField(
//                        value = currentPeminjaman.tanggalPinjam,
//                        onValueChange = { /* Read-only */ },
//                        label = { Text("Tanggal Peminjaman") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        enabled = false,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
//                            disabledBorderColor = MaterialTheme.colorScheme.outline,
//                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    )
//
//                    OutlinedTextField(
//                        value = currentPeminjaman.tanggalPengembalian,
//                        onValueChange = { /* Read-only */ },
//                        label = { Text("Tanggal Pengembalian") },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        enabled = false,
//                        colors = OutlinedTextFieldDefaults.colors(
//                            disabledTextColor = LocalContentColor.current.copy(LocalContentColor.current.alpha),
//                            disabledBorderColor = MaterialTheme.colorScheme.outline,
//                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    )
//                }
//            }
//        }
//
//    }
//}

        @Composable
        fun ReturnScreen(navController: NavController) {
            var jumlahBarang by remember { mutableStateOf("5") }
            var tanggalPinjam by remember { mutableStateOf("15/03/2025") }
            var tanggalPengembalian by remember { mutableStateOf("18/03/2025") }
            var catatan by remember { mutableStateOf("Khataman mingguan") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "Masjid Raden Patah",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = "Lowokwaru, Malang",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Borrowing Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Waktu Pengajuan",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "14 Maret 2025, 09:39",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Waktu Peminjaman",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "15 Maret 2025",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Estimasi Pengembalian",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "21 Maret 2025",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section Title
                Text(
                    text = "Rincian Peminjaman",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Icon Placeholder
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, // Placeholder, replace with custom icon if needed
                        contentDescription = "Rincian Icon",
                        tint = Color.Green,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Form Fields
                Text(
                    text = "Nama Barang*",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                Text(
                    text = "Al-Quran",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Jumlah Barang*",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                Text(
                    text = jumlahBarang,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tanggal Pinjam*",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                Text(
                    text = tanggalPinjam,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tanggal Pengembalian*",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                Text(
                    text = tanggalPengembalian,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Catatan*",
                    fontSize = 14.sp,
                    color = Color.Red
                )
                Text(
                    text = catatan,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = { /* Handle submission */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
                ) {
                    Text(
                        text = "Ajukan Pengembalian",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }