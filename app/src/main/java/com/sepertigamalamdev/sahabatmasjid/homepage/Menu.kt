//package com.sepertigamalamdev.sahabatmasjid.homepage
//
//import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Card
//import androidx.compose.material.Icon
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AddCircle
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.lazy.items
//
//@Composable
//fun MenuScreen(){
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
////            .padding(innerPadding)
//            .padding(horizontal = 16.dp)
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Greeting
//        Text(
//            text = "Assalamualaikum, Asep",
//            fontSize = 20.sp,
//            color = Color.Black,
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Menu Items using LazyColumn and LazyRow
//        val menuItems = listOf(
//            listOf("Informasi Inventaris", "Info Status"),
//            listOf("Peminjaman", "Pengembalian"),
//            listOf("Riwayat")
//        )
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(menuItems) { rowItems ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    rowItems.forEach { item ->
//                        Card(
//                            modifier = Modifier
//                                .weight(1f)
//                                .aspectRatio(1f), // Square shape
//                            backgroundColor = Color(0xFF1A3C34), // Dark green background
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(16.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = item,
//                                    fontSize = 16.sp,
//                                    color = Color.White,
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//                    // Add empty spacers to fill the row if needed
//                    if (rowItems.size < 2) {
//                        Spacer(modifier = Modifier.weight(1f))
//                    }
//                }
//            }
//        }
//    }
//}
//}