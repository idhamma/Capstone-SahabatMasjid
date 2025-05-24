package com.sepertigamalamdev.sahabatmasjid.management

import android.net.Uri // Pastikan Uri diimpor
import androidx.activity.compose.rememberLauncherForActivityResult // Untuk image picker
import androidx.activity.result.PickVisualMediaRequest // Untuk image picker
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale // Untuk .titlecase
import androidx.compose.material.icons.filled.ArrowBack // Tambahkan import
import androidx.compose.ui.text.style.TextAlign
import com.sepertigamalamdev.sahabatmasjid.peminjaman.BorrowDetailContent
import com.sepertigamalamdev.sahabatmasjid.peminjaman.OperatorMasjidPeminjamanList


import com.sepertigamalamdev.sahabatmasjid.management.User as AppUser


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorBorrowDetailScreen(
    navController: NavController,
    borrowId: String,
    peminjamanViewModel: PeminjamanViewModel = viewModel(),
    barangViewModel: BarangViewModel = viewModel()
) {
    val peminjamanFromVm = peminjamanViewModel.peminjaman.value
    val barangFromVm = barangViewModel.barang.value

    // State loading seperti sebelumnya
    var isPeminjamanLoading by remember { mutableStateOf(true) }
    var isBarangLoading by remember { mutableStateOf(false) } // Diatur true saat fetch barang dimulai
    var barangLoadAttempted by remember { mutableStateOf(false) } // True jika usaha load barang sudah dilakukan

    var isSubmittingAction by remember { mutableStateOf(false) } // Untuk tombol approve/reject/konfirmasi
    val context = LocalContext.current

    // --- Image Picker Launcher untuk Operator ---
    val pickMediaLauncherOperator = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null && peminjamanFromVm != null) {
                // Saat ini, hanya ada satu skenario operator upload: bukti ambil
                if (peminjamanFromVm.status.equals("disetujui", ignoreCase = true) &&
                    peminjamanFromVm.imageUrlBuktiPinjam.isBlank() && !isSubmittingAction) {
                    Log.d("OperatorScreen", "Operator mengunggah bukti ambil untuk peminjaman ID: ${peminjamanFromVm.id}")
                    isSubmittingAction = true
                    peminjamanViewModel.uploadBuktiAmbilAndUpdateStatus(
                        peminjamanId = peminjamanFromVm.id,
                        imageUri = uri,
                        context = context
                    ) { success, resultMsg ->
                        isSubmittingAction = false
                        if (success) {
                            Toast.makeText(context, "Bukti pengambilan oleh operator berhasil diunggah.", Toast.LENGTH_LONG).show()
                            // Data akan refresh karena LaunchedEffect di bawah atau pembaruan StateFlow
                        } else {
                            Toast.makeText(context, "Gagal unggah bukti: $resultMsg", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    )

    // Fetch Data Peminjaman (dan re-fetch jika ada aksi submit yang mengubah data)
    LaunchedEffect(borrowId, isSubmittingAction) {
        if (!isSubmittingAction) { // Hanya fetch jika tidak sedang ada aksi submit lain
            isPeminjamanLoading = true // Set true di awal fetch
            barangLoadAttempted = false
            isBarangLoading = false
            Log.d("OperatorScreen", "Memuat peminjaman ID: $borrowId")
            peminjamanViewModel.getPeminjamanById(borrowId)
        }
    }

    // Update status loading peminjaman berdasarkan peminjamanFromVm
    LaunchedEffect(peminjamanFromVm) {
        if (peminjamanFromVm != null || !isPeminjamanLoading /* jika sebelumnya true */) {
            isPeminjamanLoading = false
        }
    }

    // Fetch Data Barang
    LaunchedEffect(peminjamanFromVm?.barangid) {
        peminjamanFromVm?.let { pem ->
            if (pem.barangid.isNotBlank()) {
                isBarangLoading = true
                barangLoadAttempted = true
                Log.d("OperatorScreen", "Memuat barang ID: ${pem.barangid}")
                barangViewModel.getBarangById(pem.barangid)
            } else {
                isBarangLoading = false
                barangLoadAttempted = false // Tidak ada barangid, tidak ada usaha load
            }
        }
        if (peminjamanFromVm == null) {
            isBarangLoading = false
            barangLoadAttempted = false
        }
    }
    // Update status loading barang
    LaunchedEffect(barangFromVm, peminjamanFromVm?.barangid, barangLoadAttempted) {
        if (peminjamanFromVm?.barangid.isNullOrBlank() || barangFromVm != null || (barangLoadAttempted && !isBarangLoading) ) {
            isBarangLoading = false
        }
    }


    // --- Action Handlers untuk Approve/Reject/Konfirmasi Selesai ---
    val handleApprove: () -> Unit = approveLambda@{
        if (peminjamanFromVm == null || isSubmittingAction) return@approveLambda
        isSubmittingAction = true
        peminjamanViewModel.approvePeminjaman(peminjamanFromVm.id) { success ->
            // isSubmittingAction akan direset oleh LaunchedEffect(borrowId, isSubmittingAction)
            // yang akan memicu pengambilan data ulang
            if (success) Toast.makeText(context, "Peminjaman disetujui!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "Gagal menyetujui peminjaman.", Toast.LENGTH_SHORT).show()
            isSubmittingAction = false // Reset manual jika tidak ada navigasi/re-fetch otomatis yang diinginkan segera
        }
    }

    val handleReject: () -> Unit = rejectLambda@{
        if (peminjamanFromVm == null || isSubmittingAction) return@rejectLambda
        if (peminjamanFromVm.jumlah > 0 && peminjamanFromVm.barangid.isBlank()) {
            Toast.makeText(context, "ID Barang tidak valid untuk pengembalian stok.", Toast.LENGTH_LONG).show()
            return@rejectLambda
        }
        isSubmittingAction = true
        peminjamanViewModel.rejectPeminjaman(
            peminjamanId = peminjamanFromVm.id,
            barangId = peminjamanFromVm.barangid,
            jumlahDikembalikan = peminjamanFromVm.jumlah
        ) { success ->
            if (success) Toast.makeText(context, "Peminjaman ditolak!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "Gagal menolak peminjaman.", Toast.LENGTH_SHORT).show()
            isSubmittingAction = false
        }
    }

    val handleKonfirmasiPengembalianSelesai: () -> Unit = handleKonfirmasiPengembalianSelesai@{
        if (peminjamanFromVm == null || isSubmittingAction) return@handleKonfirmasiPengembalianSelesai
        isSubmittingAction = true
        peminjamanViewModel.konfirmasiPengembalianSelesai(peminjamanFromVm.id) { success ->
            if (success) Toast.makeText(context, "Pengembalian barang dikonfirmasi!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "Gagal mengonfirmasi pengembalian.", Toast.LENGTH_SHORT).show()
            isSubmittingAction = false
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Peminjaman oleh Operator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Logika loading utama
            if (isPeminjamanLoading && peminjamanFromVm == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (peminjamanFromVm == null) {
                Text("Data peminjaman tidak ditemukan.", modifier = Modifier.align(Alignment.Center))
            } else {
                // Data Peminjaman Ada
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f).padding(bottom = 16.dp)) { // Beri padding bawah agar tidak tertutup tombol
                        BorrowDetailContent(
                            navController = navController, // Untuk tombol back di header BorrowDetailContent
                            peminjaman = peminjamanFromVm,
                            barang = barangFromVm,
                            isBarangLoadingFailed = (peminjamanFromVm.barangid.isNotBlank() && barangFromVm == null && barangLoadAttempted && !isBarangLoading),
                            // Lambda untuk aksi user dihilangkan, karena ini layar operator
                            onUnggahBuktiAmbilClicked = { /* Tidak relevan untuk operator di sini, operator punya tombol sendiri */ },
                            onKembalikanBarangClicked = { /* Tidak relevan untuk operator di sini */ }
                        )
                    }

                    // Tombol Aksi Operator berdasarkan status
                    if (isSubmittingAction) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp))
                    } else {
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            when (peminjamanFromVm.status.lowercase(Locale.getDefault())) {
                                "diajukan" -> {
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Button(onClick = handleApprove, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))) { Text("Setujui") }
                                        Button(onClick = handleReject, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Tolak") }
                                    }
                                }
                                "disetujui" -> {
                                    if (peminjamanFromVm.imageUrlBuktiPinjam.isBlank()) {
                                        Button(
                                            onClick = {
                                                pickMediaLauncherOperator.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) { Text("Operator: Unggah Bukti Ambil & Serahkan") }
                                    } else {
                                        Text("Menunggu barang diambil atau status diubah manual oleh operator.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                        // Opsional: Tombol untuk operator jika barang sudah diserahkan tapi user belum upload/konfirm
                                        // Button(onClick = { /* Panggil ViewModel untuk update status ke 'dipinjam' */ }) { Text("Tandai Sudah Diambil") }
                                    }
                                }
                                "proses_pengembalian" -> {
                                    // BorrowDetailContent akan menampilkan peminjaman.imageUrlBuktiKembali
                                    Button(
                                        onClick = handleKonfirmasiPengembalianSelesai,
                                        modifier = Modifier.fillMaxWidth()
                                    ) { Text("Konfirmasi Barang Sudah Kembali") }
                                }
                                else -> {
                                    Text(
                                        text = "Status: ${peminjamanFromVm.status.replaceFirstChar { it.titlecase(Locale.getDefault()) }}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
// Di ManageBorrowingsTabContent.kt

@OptIn(ExperimentalMaterial3Api::class) // Diperlukan jika Card atau komponen lain dari M3 bersifat eksperimental
@Composable
fun ManageBorrowingsTabContent(
    navController: NavController,
    managedMasjids: List<Masjid>, // Menggunakan data class Masjid langsung
    peminjamanViewModel: PeminjamanViewModel
) {
    var selectedMasjidIdForManagement by remember { mutableStateOf<String?>(null) }
    var selectedMasjidName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(managedMasjids) {
        if (managedMasjids.size == 1) {
            selectedMasjidIdForManagement = managedMasjids.first().id
            selectedMasjidName = managedMasjids.first().name
        }
        // Jika ingin mempertahankan pilihan terakhir, Anda bisa menyimpannya di ViewModel atau cara lain.
    }

    if (selectedMasjidIdForManagement != null && selectedMasjidName != null) {
        OperatorMasjidPeminjamanList( // Pastikan Composable ini sudah Anda definisikan
            navController = navController,
            masjidId = selectedMasjidIdForManagement!!,
            masjidName = selectedMasjidName!!,
            viewModel = peminjamanViewModel
        )
    } else if (managedMasjids.isNotEmpty()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Pilih Masjid untuk Dikelola:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    items = managedMasjids, // --- PERBAIKAN: Sebutkan nama parameter 'items' ---
                    key = { masjid -> masjid.id } // --- TAMBAHAN: Gunakan 'key' untuk performa ---
                ) { masjid -> // 'masjid' di sini akan bertipe Masjid
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedMasjidIdForManagement = masjid.id // <-- Sekarang 'id' seharusnya dikenali
                                selectedMasjidName = masjid.name    // <-- Sekarang 'name' seharusnya dikenali
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // <-- Ini adalah M3
                    ) {
                        Text(
                            text = masjid.name, // <-- Sekarang 'name' seharusnya dikenali
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Anda belum ditugaskan untuk mengelola masjid manapun.")
        }
    }
}