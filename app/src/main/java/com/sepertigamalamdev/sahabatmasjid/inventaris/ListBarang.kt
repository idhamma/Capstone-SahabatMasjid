package com.sepertigamalamdev.sahabatmasjid.inventaris

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.sepertigamalamdev.sahabatmasjid.Routes
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import java.io.Serializable

@Composable
fun ItemListScreen(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
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
                    text = "Daftar Barang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Daftar barang menggunakan LazyColumn
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    listOf(
                        ItemData("Mic", "INV-01", 5, 10, "Baik", "Ruang Audio"),
                        ItemData("Mic", "INV-02", 3, 8, "Baik", "Ruang Audio"),
                        ItemData("Proyektor", "INV-03", 2, 5, "Baik", "Ruang Utama"),
                        ItemData("Kabel HDMI", "INV-04", 10, 15, "Baik", "Gudang"),
                        ItemData("Speaker", "INV-05", 4, 6, "Rusak", "Ruang Audio")
                    )
                ) { item ->
                    ItemCard(item, navController)
                }
            }
        }
    }
}

data class ItemData(
    val name: String,
    val inventoryCode: String,
    val availability: Number,
    val total: Number,
    val condition: String,
    val location: String
) : Serializable

@Composable
fun ItemCard(item: ItemData, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detailBarang") },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
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
                        text = item.name[0].toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Informasi barang
                Column {
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            // Navigasi ke DetailScreen dengan membawa data item
//                            val route = "${Routes.DETAIL_SCREEN}?name=${item.name}&code=${item.inventoryCode}&availability=${item.availability}&total=${item.total}&condition=${item.condition}&location=${item.location}"
                            val route = navController.navigate("detailBarang")
                            navController.navigate(route)
                        }
                    )
                    Text(
                        text = "Kode Inventaris: ${item.inventoryCode}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Jumlah Tersedia: ${item.availability} dari total ${item.total}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Kondisi Barang: ${item.condition}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Lokasi Penyimpanan: ${item.location}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Ikon navigasi
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ">",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemListScreenPreview(navController: NavHostController = rememberNavController()) {
    ItemListScreen(navController = navController)
}