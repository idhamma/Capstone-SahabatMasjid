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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.sepertigamalamdev.sahabatmasjid.management.Barang
import coil.compose.AsyncImage
import com.sepertigamalamdev.sahabatmasjid.management.BarangViewModel

// Asumsi data class Barang sudah ada
// Asumsi BarangViewModel sudah memiliki fungsi uploadBarangImageAndUpdateDatabase

@Composable
fun AddInventoryScreen(
    navController: NavController,
    masjidId: String,
    barangViewModel: BarangViewModel = viewModel() // Inject ViewModel
) {
    var name by remember { mutableStateOf("") }
    // val masjidid = masjidId // sudah ada sebagai parameter
    var kodeInventaris by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0) }
    var dapatDipinjam by remember { mutableStateOf(false) }
    // var catatan by remember { mutableStateOf("") } // 'catatan' tidak ada di data class Barang Anda, jika ada tambahkan
    var place by remember { mutableStateOf("") } // Tambahkan state untuk 'place'
    var kondisiRusak by remember { mutableStateOf(0) } // Tambahkan state untuk 'kondisi' (jumlah rusak)

    var totalText by remember { mutableStateOf("") }
    var kondisiRusakText by remember { mutableStateOf("") }


    // State untuk URI gambar yang dipilih
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Image Picker Launcher
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    val database = FirebaseDatabase.getInstance()
    val refBarang = database.getReference("barang")

    Scaffold { innerPadding ->
        Column( // Mengganti Box dengan Column agar bisa scroll semua konten
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp) // Padding keseluruhan
        ) {
            // Header (sama seperti sebelumnya)
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF34A853)).padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Text("Tambah Inventaris", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Tombol Pilih Gambar dan Pratinjau ---
            Button(
                onClick = { pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih Gambar Barang")
            }
            Spacer(modifier = Modifier.height(8.dp))
            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Pratinjau Gambar Barang",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // --- Akhir Pilih Gambar ---

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Barang*") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))
            OutlinedTextField(value = kodeInventaris, onValueChange = { kodeInventaris = it }, label = { Text("Kode Barang*") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp))

            OutlinedTextField(
                value = totalText,
                onValueChange = { newValue ->
                    totalText = newValue
                    total = newValue.toIntOrNull() ?: 0
                },
                label = { Text("Total Barang Keseluruhan*") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = kondisiRusakText,
                onValueChange = { newValue ->
                    kondisiRusakText = newValue
                    kondisiRusak = newValue.toIntOrNull() ?: 0
                },
                label = { Text("Jumlah Barang Rusak*") }, // Asumsi 'kondisi' adalah jumlah rusak
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = place, onValueChange = { place = it }, label = { Text("Lokasi Penyimpanan*") }, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                Text("Dapat Dipinjam?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = dapatDipinjam, onCheckedChange = { dapatDipinjam = it })
            }


            Button(
                onClick = {
                    val newBarangId = refBarang.push().key // Dapatkan ID unik untuk barang baru
                    if (newBarangId == null) {
                        Toast.makeText(context, "Gagal membuat ID barang baru.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Stok awal adalah total dikurangi yang rusak
                    val stokBaik = total - kondisiRusak
                    if (stokBaik < 0) {
                        Toast.makeText(context, "Jumlah rusak tidak boleh melebihi total barang.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val barangData = Barang(
                        id = newBarangId,
                        masjidid = masjidId,
                        name = name.trim(),
                        kodeInventaris = kodeInventaris.trim(),
                        stock = stokBaik, // Stok barang yang baik/tersedia
                        total = total,    // Total keseluruhan
                        kondisi = kondisiRusak, // Jumlah yang rusak
                        place = place.trim(),
                        availability = stokBaik > 0 && dapatDipinjam,
                        dapatDipinjam = dapatDipinjam,
                        imageUrl = "" // Awalnya kosong, akan diupdate setelah gambar diunggah
                    )

                    // 1. Simpan data barang awal ke Firebase
                    refBarang.child(newBarangId).setValue(barangData)
                        .addOnSuccessListener {
                            // 2. Jika ada gambar yang dipilih, unggah dan update field imageUrl
                            imageUri?.let { uri ->
                                barangViewModel.uploadBarangImageAndUpdateDatabase(
                                    barangId = newBarangId,
                                    masjidId = masjidId,
                                    imageUri = uri,
                                    context = context,
                                    bucketName = "product-images" // Sesuaikan nama bucket
                                ) {
                                  success, newUrlOrError ->
                                    if (success) {
                                        Toast.makeText(context, "Barang dan gambar berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, "Barang ditambahkan, tapi gagal unggah gambar: $newUrlOrError", Toast.LENGTH_LONG).show()
                                        navController.popBackStack() // Tetap kembali, barang sudah ada walau tanpa gambar
                                    }
                                }
                            } ?: run {
                                // Tidak ada gambar dipilih, barang sudah tersimpan
                                Toast.makeText(context, "Barang berhasil ditambahkan (tanpa gambar)!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Gagal menyimpan data barang: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
            ) {
                Text("Simpan", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}