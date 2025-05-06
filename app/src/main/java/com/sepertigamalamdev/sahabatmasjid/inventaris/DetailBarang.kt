package com.sepertigamalamdev.sahabatmasjid.inventaris

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.homepage.HomepageScreen


@Composable
fun DetailBarangScreen(navController: NavController) {

    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header dengan tombol kembali dan judul
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

            // Detail Barang
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
                            text = "M",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Informasi barang
                    Column {
                        Text(
                            text = "Mic",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Kode Inventaris: INV-01",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Jumlah Tersedia: 5 unit dari 10 total",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Kondisi Barang: Baik",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Lokasi Penyimpanan: Ruang Audio",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Form Input Jumlah Barang
            Text(
                text = "Ingin pinjam barang",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Silahkan Ajukan ke menu peminjaman barang",
                fontSize =  16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )

            // Tombol Ajukan Peminjaman
            Button(
                onClick = { navController.navigate("pengajuanPeminjamanLanjut")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Ajukan Peminjaman",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailBarangScreenPreview(navController: NavHostController = rememberNavController()) {
    DetailBarangScreen(navController = navController)
}