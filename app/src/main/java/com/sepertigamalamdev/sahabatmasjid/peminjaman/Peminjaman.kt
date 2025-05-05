package com.sepertigamalamdev.sahabatmasjid.peminjaman


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PeminjamanScreen() {
    var historyWindow by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header dengan tab "Dalam Proses" dan "Riwayat"
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dalam Proses",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (!historyWindow) Color.Black else Color.Gray,
                modifier = Modifier.clickable {
                    historyWindow = false
                }
            )
            Spacer(modifier = Modifier.width(96.dp))
            Text(
                text = "Riwayat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (historyWindow) Color.Black else Color.Gray,
                modifier = Modifier.clickable {
                    historyWindow = true
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daftar item menggunakan LazyColumn
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(listOf(
//                "24 Mei 2025 - 8 Jun 2025",
//                "24 Mei 2025 - 8 Jun 2025",
//                "24 Mei 2025 - 8 Jun 2025",
//                "24 Mei 2025 - 8 Jun 2025",
//                "24 Mei 2025 - 8 Jun 2025"
//            )) { date ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column {
//                        Text(
//                            text = "Masjid Raden Patah",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = date,
//                            fontSize = 14.sp,
//                            color = Color.Gray
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .background(Color.LightGray, CircleShape)
//                    )
//                }
//            }

            if(!historyWindow) {
                LazyColumn {
//                    items(orders) { order ->
//                        Borrow(order, navController)
//                    }
                    items(listOf(
                        "24 Mei 2025 - 8 Jun 2025",
                        "24 Mei 2025 - 8 Jun 2025",
                        "24 Mei 2025 - 8 Jun 2025",
                        "24 Mei 2025 - 8 Jun 2025",
                        "24 Mei 2025 - 8 Jun 2025"
                    )) { date ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Masjid Raden Patah",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = date,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.LightGray, CircleShape)
                            )
                        }
                    }
                }
            }
            else {
                LazyColumn {
//                    items(orders) { order ->
//                        CompletedOrderCard(order, navController)
//                    }

                    items(
                        listOf(
                            "24 Mei 2025 - 8 Jun 2025",
                            "24 Mei 2025 - 8 Jun 2025",
                            "24 Mei 2025 - 8 Jun 2025",
                            "24 Mei 2025 - 8 Jun 2025",
                            "24 Mei 2025 - 8 Jun 2025"
                        )
                    ) { date ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Masjid Raden Patah 2",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = date,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.LightGray, CircleShape)
                            )
                        }
                    }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PeminjamanScreenPreview(){
    PeminjamanScreen()
}