package com.sepertigamalamdev.sahabatmasjid.management.barang

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import coil.compose.AsyncImage // Untuk pratinjau gambar
import com.sepertigamalamdev.sahabatmasjid.management.BarangViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangEditSection(
    navController: NavController,
    itemId: String,
    barangViewModel: BarangViewModel = viewModel() // Inject ViewModel
) {
    val context = LocalContext.current
    val barangRefNode = FirebaseDatabase.getInstance().getReference("barang") // Ubah nama variabel agar tidak konflik
    var barangState by remember { mutableStateOf<Barang?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // State untuk URI gambar baru yang dipilih
    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker Launcher
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> newImageUri = uri }
    )

    LaunchedEffect(itemId) {
        isLoading = true
        barangRefNode.child(itemId).addValueEventListener(object : ValueEventListener { // Gunakan addValueEventListener untuk update otomatis
            override fun onDataChange(snapshot: DataSnapshot) {
                barangState = snapshot.getValue(Barang::class.java)
                isLoading = false
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal ambil data: ${error.message}")
                Toast.makeText(context, "Gagal memuat data barang.", Toast.LENGTH_SHORT).show()
                isLoading = false
                // navController.popBackStack() // Jangan pop stack jika hanya gagal listener awal
            }
        })
        // Ingat untuk menghapus listener di onDispose jika menggunakan addValueEventListener di LaunchedEffect yang kompleks
        // Namun, karena ini adalah state di ViewModel (jika getBarangById mengupdate _barang), itu lebih baik.
        // Jika getBarangById di ViewModel tidak menggunakan StateFlow/LiveData untuk _barang,
        // maka addValueEventListener di sini perlu penanganan onDispose.
        // Untuk contoh ini, kita asumsikan _barang di ViewModel akan terupdate dari luar jika perlu.
        // Cara yang lebih baik: barangViewModel.getBarangById(itemId) dan observasi barangViewModel.barang.value
    }
    // Jika ingin mengambil data hanya sekali, addListenerForSingleValueEvent sudah benar.
    // Jika ingin data real-time di layar edit ini, addValueEventListener adalah pilihan,
    // tapi pastikan untuk menghapus listener di onDispose DisposableEffect.

    // Ambil data dari ViewModel jika Anda ingin memisahkannya dari LaunchedEffect di atas
    // LaunchedEffect(itemId) {
    //     barangViewModel.getBarangById(itemId)
    // }
    // val barangFromVm = barangViewModel.barang.value // Observasi ini


    Scaffold( bottomBar = { Footer(navController) } ) { innerPadding ->
        Box( modifier = Modifier.fillMaxSize().padding(innerPadding) ) {
            if (isLoading && barangState == null) { // Tampilkan loading jika barangState masih null dan isLoading true
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (barangState == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Barang tidak ditemukan.")
                }
            } else {
                val currentBarang = barangState!! // Barang dijamin non-null di sini

                // State untuk field-field form, diinisialisasi dari currentBarang
                // dan diupdate jika currentBarang berubah (misalnya dari listener Firebase)
                val nameState = remember(currentBarang.name) { mutableStateOf(currentBarang.name) }
                val kodeState = remember(currentBarang.kodeInventaris) { mutableStateOf(currentBarang.kodeInventaris) }
                val stockTextState = remember(currentBarang.stock) { mutableStateOf(currentBarang.stock.toString()) }
                val totalTextState = remember(currentBarang.total) { mutableStateOf(currentBarang.total.toString()) }
                val kondisiTextState = remember(currentBarang.kondisi) { mutableStateOf(currentBarang.kondisi.toString()) }
                val placeState = remember(currentBarang.place) { mutableStateOf(currentBarang.place) }
                val dapatDipinjamState = remember(currentBarang.dapatDipinjam) { mutableStateOf(currentBarang.dapatDipinjam) }

                // State untuk menampilkan URL gambar saat ini atau pratinjau URI baru
                val displayImageSource: Any? = newImageUri ?: currentBarang.imageUrl.ifEmpty { null }


                Column( modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()) ) {
                    // Header (sama)
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") }
                        Text("Edit Barang", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        // --- Tampilan Gambar Saat Ini dan Tombol Ubah ---
                        if (displayImageSource != null) {
                            AsyncImage(
                                model = displayImageSource,
                                contentDescription = "Gambar Barang",
                                modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.LightGray, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                Text("Tidak ada gambar")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (currentBarang.imageUrl.isNotBlank() || newImageUri != null) "Ubah Gambar" else "Tambah Gambar")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // --- Akhir Tampilan Gambar ---

                        FieldData("Nama Barang", "Masukkan nama barang", nameState)
                        FieldData("Kode Inventaris", "Masukkan kode", kodeState)
                        FieldData("Jumlah Stok Baik", "Masukkan jumlah stok yang baik", stockTextState)
                        FieldData("Total Barang Keseluruhan", "Masukkan total barang", totalTextState)
                        FieldData("Jumlah Barang Rusak", "Masukkan jumlah rusak", kondisiTextState)
                        FieldData("Tempat", "Masukkan lokasi penyimpanan", placeState)
                        // ... (Switch Dapat Dipinjam seperti sebelumnya) ...
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text("Dapat Dipinjam", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.weight(1f))
                            Switch(
                                checked = dapatDipinjamState.value,
                                onCheckedChange = { dapatDipinjamState.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                            Text(if (dapatDipinjamState.value) "Ya" else "Tidak", modifier = Modifier.padding(start = 8.dp))
                        }


                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                val stockValue = stockTextState.value.toIntOrNull() ?: 0
                                val totalValue = totalTextState.value.toIntOrNull() ?: 0
                                val kondisiValue = kondisiTextState.value.toIntOrNull() ?: 0

                                // Fungsi untuk melakukan update ke Firebase setelah URL gambar (jika ada) siap
                                val performFirebaseUpdate = { finalImageUrl: String ->
                                    val updatedBarangMap = mapOf(
                                        "name" to nameState.value.trim(),
                                        "kodeInventaris" to kodeState.value.trim(),
                                        "stock" to stockValue,
                                        "total" to totalValue,
                                        "kondisi" to kondisiValue,
                                        "place" to placeState.value.trim(),
                                        "dapatDipinjam" to dapatDipinjamState.value,
                                        "availability" to (stockValue > 0 && dapatDipinjamState.value),
                                        "imageUrl" to finalImageUrl
                                    )
                                    barangRefNode.child(itemId).updateChildren(updatedBarangMap)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Data barang berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                            newImageUri = null // Reset URI gambar baru setelah berhasil
                                            navController.popBackStack()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Gagal memperbarui data barang: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }

                                // Cek apakah ada gambar baru yang dipilih
                                newImageUri?.let { uri ->
                                    // Ada gambar baru, unggah dulu
                                    Toast.makeText(context, "Mengunggah gambar...", Toast.LENGTH_SHORT).show()
                                    barangViewModel.uploadBarangImageAndUpdateDatabase(
                                        barangId = itemId,
                                        masjidId = currentBarang.masjidid,
                                        imageUri = uri,
                                        context = context,
                                        bucketName = "product-images" // Sesuaikan
                                    ) { success, newUrlOrError ->
                                        if (success && newUrlOrError != null) {
                                            // Gambar berhasil diunggah, lanjutkan update data lain dengan URL baru
                                            // `uploadBarangImageAndUpdateDatabase` sudah mengupdate imageUrl di Firebase.
                                            // Jadi, kita cukup update field lain jika perlu, atau biarkan listener yang handle.
                                            // Untuk konsistensi, kita bisa panggil update umum di sini dengan URL baru,
                                            // atau pastikan `uploadBarangImageAndUpdateDatabase` hanya update imageUrl,
                                            // dan fungsi di bawah ini update sisanya.
                                            // Untuk saat ini, asumsikan `uploadBarangImageAndUpdateDatabase` sudah update imageUrl.
                                            // Kita tetap panggil `performFirebaseUpdate` untuk field lain,
                                            // dan `finalImageUrl` akan jadi `newUrlOrError`.
                                            performFirebaseUpdate(newUrlOrError)
                                        } else {
                                            Toast.makeText(context, "Gagal unggah gambar baru. Perubahan lain tetap disimpan.", Toast.LENGTH_LONG).show()
                                            // Jika gambar gagal diunggah, tetap simpan perubahan field lain dengan URL gambar lama
                                            performFirebaseUpdate(currentBarang.imageUrl)
                                        }
                                    }
                                } ?: run {
                                    // Tidak ada gambar baru dipilih, langsung update field lain dengan URL gambar lama
                                    performFirebaseUpdate(currentBarang.imageUrl)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Simpan Perubahan")
                        }
                        // ... (Tombol Hapus dan Dialog seperti sebelumnya) ...
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Text("Hapus Barang", color = MaterialTheme.colorScheme.onError)
                        }
                    }
                }
                // Dialog konfirmasi hapus
                if (showDeleteDialog && barangState != null) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Konfirmasi Hapus") },
                        text = { Text("Apakah Anda yakin ingin menghapus barang '${barangState?.name ?: "ini"}'?") },
                        confirmButton = {
                            Button(onClick = {
                                showDeleteDialog = false
                                val barangNameToDelete = barangState?.name ?: "Barang ini"
                                barangRefNode.child(itemId).removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Barang '$barangNameToDelete' berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Gagal menghapus barang: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }) { Text("Hapus") }
                        },
                        dismissButton = { OutlinedButton(onClick = { showDeleteDialog = false }) { Text("Batal") } }
                    )
                }
            }
        }
    }
}


// Composable FieldData (tetap sama seperti yang Anda berikan)
@Composable
fun FieldData(label: String, placeholder: String, valueState: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = valueState.value,
            onValueChange = { valueState.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)) // Pertimbangkan menggunakan warna dari MaterialTheme
                .padding(horizontal = 16.dp, vertical = 12.dp), // Padding yang lebih standar
            singleLine = true, // Biasanya field data satu baris kecuali deskripsi
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) { // Untuk alignment placeholder
                    if (valueState.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}