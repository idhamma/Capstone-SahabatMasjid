package com.sepertigamalamdev.sahabatmasjid.management

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

    var isPeminjamanLoading by remember { mutableStateOf(true) }
    var isBarangLoading by remember { mutableStateOf(false) }
    var barangLoadAttempted by remember { mutableStateOf(false) }
    var isSubmittingAction by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(borrowId, isSubmittingAction) {
        if (!isSubmittingAction) {
            isPeminjamanLoading = false
            barangLoadAttempted = false
            isBarangLoading = false
            peminjamanViewModel.getPeminjamanById(borrowId)
        }
    }

    LaunchedEffect(peminjamanFromVm?.barangid) {
        peminjamanFromVm?.let { pem ->
            if (pem.barangid.isNotBlank()) {
                barangViewModel.getBarangById(pem.barangid)
                isBarangLoading = true
                barangLoadAttempted = true
            } else {
                isBarangLoading = false
                barangLoadAttempted = false
            }
        }
        if (peminjamanFromVm == null) {
            isBarangLoading = false
            barangLoadAttempted = false
        }
    }

    val handleApprove: () -> Unit = approveLambda@{ // <--- Tambahkan label di sini
        if (peminjamanFromVm == null || isSubmittingAction) {
            Log.d("OperatorScreen", "Approve skipped: peminjamanFromVm is null or isSubmittingAction is true.")
            return@approveLambda // <--- Gunakan return dengan label
        }
        isSubmittingAction = true
        Log.d("OperatorScreen", "Calling approvePeminjaman for ID: ${peminjamanFromVm.id}")
        peminjamanViewModel.approvePeminjaman(peminjamanFromVm.id) { success ->
            if (success) {
                Toast.makeText(context, "Peminjaman disetujui!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Gagal menyetujui peminjaman.", Toast.LENGTH_SHORT).show()
            }
            isSubmittingAction = false
        }
    }

    val handleReject: () -> Unit = rejectLambda@{ // <--- Tambahkan label di sini
        if (peminjamanFromVm == null || isSubmittingAction) {
            Log.d("OperatorScreen", "Reject skipped: peminjamanFromVm is null or isSubmittingAction is true.")
            return@rejectLambda // <--- Gunakan return dengan label
        }
        if (peminjamanFromVm.jumlah > 0 && peminjamanFromVm.barangid.isBlank()) {
            Toast.makeText(context, "ID Barang tidak valid untuk pengembalian stok.", Toast.LENGTH_LONG).show()
            return@rejectLambda // <--- Gunakan return dengan label
        }

        isSubmittingAction = true
        Log.d("OperatorScreen", "Calling rejectPeminjaman for ID: ${peminjamanFromVm.id}")
        peminjamanViewModel.rejectPeminjaman(
            peminjamanId = peminjamanFromVm.id,
            barangId = peminjamanFromVm.barangid,
            jumlahDikembalikan = peminjamanFromVm.jumlah
        ) { success ->
            if (success) {
                Toast.makeText(context, "Peminjaman ditolak!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Gagal menolak peminjaman.", Toast.LENGTH_SHORT).show()
            }
            isSubmittingAction = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Peminjaman") },
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
            val showInitialLoader = peminjamanFromVm == null && !isPeminjamanLoading
            val peminjamanNotFound = peminjamanFromVm == null && !isPeminjamanLoading && !isSubmittingAction // Ditambah !isSubmittingAction

            if (showInitialLoader && !peminjamanNotFound) { // Hanya tampilkan loader jika belum dianggap "not found"
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (peminjamanNotFound) {
                Text("Data peminjaman tidak ditemukan.", modifier = Modifier.align(Alignment.Center))
            }
            else if (peminjamanFromVm != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        val isBarangEffectivelyLoadingFailedOrNotAvailable =
                            peminjamanFromVm.barangid.isNotBlank() && barangLoadAttempted && barangFromVm == null

                        BorrowDetailContent(
                            navController = navController,
                            peminjaman = peminjamanFromVm,
                            barang = barangFromVm,
                            isBarangLoadingFailed = isBarangEffectivelyLoadingFailedOrNotAvailable
                        )
                    }

                    if (peminjamanFromVm.status.equals("diajukan", ignoreCase = true)) {
                        if (isSubmittingAction) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp))
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(onClick = handleApprove, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))) { Text("Setujui") }
                                Button(onClick = handleReject, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Tolak") }
                            }
                        }
                    } else {
                        Text(
                            text = "Status: ${peminjamanFromVm.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
                            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 20.dp)
                        )
                    }
                }
            } else {
                // Fallback jika kondisi tidak terpenuhi (seharusnya tidak banyak terjadi dengan logika di atas)
                if (isPeminjamanLoading || isSubmittingAction) { // Jika masih ada proses loading yang mungkin
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Text("Silakan coba lagi.", modifier = Modifier.align(Alignment.Center)) // Pesan fallback umum
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