package com.sepertigamalamdev.sahabatmasjid.management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer

@Composable
fun AddInventoryScreen(navController: NavController, masjidId : String) {

    /*
    data class Barang(
    var id: String = "",
    var masjidid: String = "",
    var name: String = "",
    var kodeInventaris: String = "",
    var stock: Int = 0,
    var total: Int = 0,
    var kondisi: Int = 0,
    var place: String = "",
    var availability: Boolean = false, //availability = true if stock > 0 && dapatDipinjam == true
    var dapatDipinjam: Boolean = false,
    var imageUrl: String = "",
)
     */

//
//    var namaBarang by remember { mutableStateOf("Kabel") }
//    var kategoriBarang by remember { mutableStateOf("Alat Rumah Tangga") }
//    var kodeBarang by remember { mutableStateOf("INV-03") }
//    var jumlahBarang by remember { mutableStateOf("3") }

    var name by remember { mutableStateOf("") }
    val masjidid = masjidId
    var kodeInventaris by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(0) }
    var dapatDipinjam by remember {mutableStateOf(false)}
    var catatan by remember { mutableStateOf("") }
    var totalText by remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance()
    val refBarang = database.getReference("barang")

    Scaffold(
//        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
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
                        text = "Tambah Inventaris",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Icon
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ShoppingCart,
//                        contentDescription = "Shopping Cart Icon",
//                        tint = Color.Green,
//                        modifier = Modifier
//                            .background(Color(0xFF34A853), RoundedCornerShape(8.dp))
//                            .padding(16.dp)
//                    )
//                }

                // Form Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Barang*", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )

                OutlinedTextField(
                    value = kodeInventaris,
                    onValueChange = { kodeInventaris = it },
                    label = { Text("Kode Barang*", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = totalText,
                    onValueChange = { newValue ->
                        totalText = newValue
                        // Convert the input to Int, default to 0 if invalid
                        total = newValue.toIntOrNull() ?: 0
                    },
                    label = { Text("Jumlah Barang*", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan*", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Save Button
                Button(
                    onClick = {
                        val key = refBarang.push().key
                        key?.let {
                            val barang = Barang(
                                id = it,
                                masjidid = masjidid,
                                name = name,
                                kodeInventaris = kodeInventaris,
                                stock = total,
                                total = total,
                                kondisi = 10,
                                place = "Belum ditentukan",
                                availability = total > 0 && dapatDipinjam,
                                dapatDipinjam = dapatDipinjam,
                                imageUrl = "",
                            )
                            refBarang.child(it).setValue(barang)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
                ) {
                    Text(
                        text = "Simpan",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
    }

