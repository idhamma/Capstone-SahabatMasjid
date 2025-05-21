package com.sepertigamalamdev.sahabatmasjid.management.barang

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang

@Composable
fun BarangEditSection(navController: NavController, itemId: String) {
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
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(            modifier = Modifier
                    .fillMaxSize())
                {
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
                            text = "Edit Barang",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }



                    barang?.let { data ->
                        val nameState = remember { mutableStateOf(data.name) }
                        val kodeState = remember { mutableStateOf(data.kodeInventaris) }
                        val stockState = remember { mutableStateOf(data.stock.toString()) }
                        val totalState = remember { mutableStateOf(data.total.toString()) }
                        val kondisiState = remember { mutableStateOf(data.kondisi.toString()) }
                        val placeState = remember { mutableStateOf(data.place) }
                        val dapatDipinjamState = remember { mutableStateOf(data.dapatDipinjam) }

                        Column(modifier = Modifier.padding(16.dp)) {
                            FieldData("Nama Barang", "Masukkan nama barang", nameState)
                            FieldData("Kode Inventaris", "Masukkan kode", kodeState)
                            FieldData("Jumlah Stok", "Masukkan jumlah stok", stockState)
                            FieldData("Total Barang", "Masukkan total barang", totalState)
                            FieldData("Jumlah Rusak", "Masukkan jumlah rusak", kondisiState)
                            FieldData("Tempat", "Masukkan lokasi penyimpanan", placeState)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Dapat Dipinjam",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Switch(
                                    checked = dapatDipinjamState.value,
                                    onCheckedChange = { dapatDipinjamState.value = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                                )
                                Text(
                                    text = if (dapatDipinjamState.value) "Ya" else "Tidak",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    val updatedBarang = mapOf(
                                        "name" to nameState.value,
                                        "kodeInventaris" to kodeState.value,
                                        "stock" to (stockState.value.toIntOrNull() ?: 0),
                                        "total" to (totalState.value.toIntOrNull() ?: 0),
                                        "kondisi" to (kondisiState.value.toIntOrNull() ?: 0),
                                        "place" to placeState.value,
                                        "dapatDipinjam" to dapatDipinjamState.value,
                                        "availability" to ((stockState.value.toIntOrNull()
                                            ?: 0) > 0 && dapatDipinjamState.value)
                                    )

                                    FirebaseDatabase.getInstance()
                                        .getReference("barang")
                                        .child(itemId) // gunakan ID yang sudah ada
                                        .updateChildren(updatedBarang)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Data barang berhasil diperbarui!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack() // kembali ke halaman sebelumnya
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Gagal memperbarui data barang.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Simpan Perubahan", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

    }
}


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
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp),
            decorationBox = { innerTextField ->
                Box {
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

//
//@Composable
//fun DetailEditScreen(){
//    val barang = Barang(
//        name = "Proyektor Epson",
//        kodeInventaris = "INV-001",
//        stock = 3,
//        total = 5,
//        kondisi = 2,
//        place = "Ruang Multimedia",
//        availability = true,
//        dapatDipinjam = true
//    )
//
//    BarangEditSection(barang = barang)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DetailEditScreenPreview(){
//    DetailEditScreen()
//}

