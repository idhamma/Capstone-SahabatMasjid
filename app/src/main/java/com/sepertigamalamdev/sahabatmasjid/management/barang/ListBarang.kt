package com.sepertigamalamdev.sahabatmasjid.management.barang

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//import com.sepertigamalamdev.sahabatmasjid.Routes
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang

@Composable
fun ItemListScreen(navController: NavController, borrow: Boolean = false, masjidid: String) {
    val database = FirebaseDatabase.getInstance().getReference("barang")
    val barangList = remember { mutableStateListOf<Barang>() }

    LaunchedEffect(Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                barangList.clear()
                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(Barang::class.java)

                    // ❗ Filter berdasarkan masjidid
                    if (barang?.masjidid == masjidid) {
                        if (borrow) {
                            if (barang.availability == true && barang.dapatDipinjam == true && barang.stock > 0) {
                                barangList.add(barang)
                            }
                        } else {
                            barangList.add(barang)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal ambil data: ${error.message}")
            }
        })
    }

    Scaffold(bottomBar = { Footer(navController) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
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

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(barangList) { barang ->
                        BarangCard(barang, navController)
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("tambahBarang/$masjidid") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF34A853),
                contentColor = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }

        }
    }
}
@Composable
fun BarangCard(barang: Barang, navController: NavController) {
    var id = barang.id
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Navigasi ke detail barang, kirimkan id barang
                if (!id.isNullOrBlank()) {
                    navController.navigate("detailBarang/$id")
                } else {
                    Log.e("NAVIGATION", "Barang ID is null or blank")

                    navController.navigate("landing")
                }
            },
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = barang.name.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = barang.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Kode Inventaris: " + barang.kodeInventaris,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    // Navigasi ke detail barang dengan ID barang
                    if (!id.isNullOrBlank()) {
                        navController.navigate("detailBarang/$id")
                    } else {
                        Log.e("NAVIGATION", "Barang ID is null or blank")

                        navController.navigate("landing")
                    }
                }
            )
        }
    }
}