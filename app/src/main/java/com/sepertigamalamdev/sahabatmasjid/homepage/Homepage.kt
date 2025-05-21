package com.sepertigamalamdev.sahabatmasjid.homepage

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.management.MasjidViewModel
import com.sepertigamalamdev.sahabatmasjid.management.UserViewModel

@Composable
//fun HomepageScreen(){
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
//            text = "Assalamualaikum, Jamaah",
//            fontSize = 20.sp,
//            color = Color.White,
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Search Bar
//        OutlinedTextField(
//            value = "",
//            onValueChange = { /* Static for now */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White, RoundedCornerShape(8.dp)),
//            placeholder = { Text("Cari Inventaris") },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search",
//                    tint = Color.Gray
//                )
//            },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.MoreVert, //sementara
//                    contentDescription = "Filter",
//                    tint = Color.Gray
//                )
//            },
//            readOnly = true
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Image Placeholder
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(150.dp)
//                .background(Color.Gray, RoundedCornerShape(8.dp))
//        ) {
//            Text(
//                text = "Image Placeholder",
//                color = Color.White,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Info Status Section
//        Text(
//            text = "INFO STATUS",
//            fontSize = 18.sp,
//            color = Color.White,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF1A3C34), RoundedCornerShape(8.dp))
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            // Tersedia
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Icon(
//                    imageVector = Icons.Default.CheckCircle,
//                    contentDescription = "Tersedia",
//                    tint = Color.Green,
//                    modifier = Modifier.size(40.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "Tersedia",
//                    fontSize = 14.sp,
//                    color = Color.White
//                )
//                Text(
//                    text = "25 Barang",
//                    fontSize = 16.sp,
//                    color = Color.White
//                )
//            }
//
//            // Dipinjam
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Icon(
//                    imageVector = Icons.Default.AddCircle, //sementara
//                    contentDescription = "Dipinjam",
//                    tint = Color.White,
//                    modifier = Modifier.size(40.dp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "Dipinjam",
//                    fontSize = 14.sp,
//                    color = Color.White
//                )
//                Text(
//                    text = "5 Barang",
//                    fontSize = 16.sp,
//                    color = Color.White
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Riwayat Section
//        Text(
//            text = "Riwayat",
//            fontSize = 18.sp,
//            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
//            color = Color.DarkGray,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        // Mic History
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp),
//            backgroundColor = Color.LightGray,
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Info, //sementara
//                    contentDescription = "Mic",
//                    tint = Color.Gray,
//                    modifier = Modifier.size(40.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Column {
//                    Text(
//                        text = "MIC",
//                        fontSize = 16.sp,
//                        color = Color.Black
//                    )
//                    Text(
//                        text = "Dipinjam",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                    Text(
//                        text = "01/04/2025 - 02/04/2025",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//        }
//
//        // Sound System History
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            backgroundColor = Color.LightGray,
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Info, //sementara
//                    contentDescription = "Sound System",
//                    tint = Color.Gray,
//                    modifier = Modifier.size(40.dp)
//                )
//                Spacer(modifier = Modifier.width(16.dp))
//                Column {
//                    Text(
//                        text = "SOUND SYSTEM",
//                        fontSize = 16.sp,
//                        color = Color.Black
//                    )
//                    Text(
//                        text = "Dikembalikan",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                    Text(
//                        text = "25/03/2025 - 27/03/2025",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//        }
//
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        Footer()
//    }
//
//
//
//}

fun HomepageScreen(navController: NavController,masjidId : String) {
    val viewModel: UserViewModel = viewModel()
    val masjidViewModel: MasjidViewModel = viewModel()

    var nickname by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var userStatus by remember { mutableStateOf<String?>(null) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val context = LocalContext.current

    // Ambil role
    LaunchedEffect(Unit) {
        viewModel.getUserRole(uid, masjidId) { status ->
            userStatus = status
        }
    }

    // Ambil data masjid
    LaunchedEffect(masjidId) {
        masjidViewModel.fetchMasjidData(masjidId)
    }

    // Listener user data
    DisposableEffect(uid) {
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(uid)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nickname = snapshot.child("nickname").getValue(String::class.java) ?: ""
                    isLoading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }

            userRef.addValueEventListener(listener)
            onDispose { userRef.removeEventListener(listener) }
        } else {
            navController.navigate("Login")
        }
        onDispose { }
    }

    val masjid by masjidViewModel.masjid

    Scaffold(
        bottomBar = { Footer(navController = navController) }
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
                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Salam
                Text(
                    text = "Assamualaikum,",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = nickname,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )


                // Alamat
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = masjid.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = masjid.alamat,
                        fontSize = 14.sp
                    )
                }

                // Tabs menggunakan LazyRow
                if(userStatus == "operator"){
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        items(4) { index ->
                            val tabName = when (index) {
                                0 -> "Takmir"
                                1 -> "Inventaris"
                                2 -> "Jadwal Petugas"
                                else -> "kelola jemaah"
                            }
                            val destination = when (index) {
                                0 -> "tambahBarang/$masjidId"   // Anda bisa menentukan rute untuk Takmir
                                1 -> "inventaris/${false}/$masjidId" // Rute untuk Inventaris
                                2 -> "jadwal_petugas" // Anda bisa menentukan rute untuk Jadwal Petugas
                                else -> "manageJemaah/$masjidId"
                            }
                            Button(
                                onClick = { navController.navigate(destination) },
                                modifier = Modifier
                                    .size(80.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                            ) {
                                Text(
                                    text = tabName,
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        items(3) { index ->
                            val tabName = when (index) {
                                0 -> "Takmir"
                                1 -> "Inventaris"
                                else -> "Jadwal Petugas"
                            }
                            val destination = when (index) {
                                0 -> "takmir"   // Anda bisa menentukan rute untuk Takmir
                                1 -> "inventaris" // Rute untuk Inventaris
                                else -> "jadwal_petugas" // Anda bisa menentukan rute untuk Jadwal Petugas
                            }
                            Button(
                                onClick = { navController.navigate(destination) },
                                modifier = Modifier
                                    .size(80.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                            ) {
                                Text(
                                    text = tabName,
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }



                    // Alur Peminjaman Barang
                    Text(
                        text = "Alur Peminjaman Barang",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(text = "Tata cara peminjaman barang :", fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "1. Cek ketersediaan barang", fontSize = 14.sp)
                            Text(text = "2. Pilih barang beserta jumlahnya", fontSize = 14.sp)
                            Text(text = "3. Ajukan peminjaman disetujui takmir", fontSize = 14.sp)
                            Text(text = "4. Tunggu pengajuan disetujui", fontSize = 14.sp)

                            Spacer(modifier = Modifier.height(150.dp))
                            if (userStatus == "jemaah") {
                                Button(
                                    onClick = { navController.navigate("inventaris/${true}/$masjidId") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(text = "Ajukan Peminjaman", fontSize = 16.sp)
                                }
                            }

                            else if(userStatus == "operator"){
                                Button(
                                    onClick = { navController.navigate("inventaris/${true}/$masjidId") }, //belum selesai, nanti ada khusus operator
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(text = "Ajukan Peminjaman", fontSize = 16.sp)
                                }
                            }
                            else{

                                Text(text = "Anda bukan jemaah masjid ini, silahkan ajukan menjadi jemaah jika ingin meminjam barang", fontSize = 14.sp)
                                Button(
                                    onClick = { navController.navigate("applyJemaah/$masjidId") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(text = "Ajukan jadi Jemaah", fontSize = 16.sp)
                                }
                            }
                        }

                    }
            }
        }
    }
}