package com.sepertigamalamdev.sahabatmasjid.peminjaman


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.sepertigamalamdev.sahabatmasjid.management.Peminjaman
import androidx.navigation.compose.rememberNavController


@Composable
fun DetailPeminjaman() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header dengan tombol kembali dan judul
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(40.dp))

            Column (
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Masjid Raden Patah",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "LOWOKWARU, MALANG",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Waktu Pengajuan",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Waktu Peminjaman",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Estimasi Pengembalian",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

            }
            Column {

                Text(
                    text = "15 Maret 2025",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "21 Maret 2025",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "21 Maret 2025",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rincian Peminjaman Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
            Text(
                text = "Rincian Peminjaman",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Detail Peminjaman
        FormField(label = "Nama Barang *", placeholder = "Nama Barang")
        FormField(label = "Jumlah Barang *", placeholder = "Jumlah Barang")
        FormField(label = "Tanggal Pinjam *", placeholder = "DD/MM/YYYY")
        FormField(label = "Tanggal Pengembalian *", placeholder = "DD/MM/YYYY")
        FormField(label = "Catatan *", placeholder = "Keterangan keperluan")


        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(text = "Ajukan Pengembalian", fontSize = 16.sp)
        }
    }
}

@Composable
fun FormField(label: String, placeholder: String) {
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
            value = "",
            onValueChange = { /* Handle input */ },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp),
            decorationBox = { innerTextField ->
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DetailPeminjamanPreview(){
//    val navController = NavController(LocalContext.current)
//    DetailBorrowScreen(navController = navController, borrowId = "123")
//}

//@Composable
//fun DetailBorrowScreen(
//    navController: NavController,
//    borrowId: String
//) {
//    val context = LocalContext.current
//    val database = FirebaseDatabase.getInstance()
//    val databaseBarang = database.getReference("barang")
//    val databasePeminjaman = database.getReference("peminjaman")
//
//    var peminjaman by remember { mutableStateOf<Peminjaman?>(null) }
//    var barang by remember { mutableStateOf<Barang?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//    var itemId by remember { mutableStateOf("") }
//
////    LaunchedEffect(borrowId) {
////        isLoading = true
////
////        databasePeminjaman.child(borrowId)
////            .addListenerForSingleValueEvent(object : ValueEventListener {
////                override fun onDataChange(snapshot: DataSnapshot) {
////                    try {
////                        val peminjamanData = snapshot.getValue(Peminjaman::class.java)
////                        if (peminjamanData != null) {
////                            peminjaman = peminjamanData
////                            itemId = peminjamanData.barangid
////
////                            // Ambil data barang berdasarkan itemId
////                            databaseBarang.child(itemId)
////                                .addListenerForSingleValueEvent(object : ValueEventListener {
////                                    override fun onDataChange(barangSnapshot: DataSnapshot) {
////                                        barang = barangSnapshot.getValue(Barang::class.java)
////                                        isLoading = false
////                                    }
////
////                                    override fun onCancelled(error: DatabaseError) {
////                                        Log.e("Firebase", "Gagal ambil data barang: ${error.message}")
////                                        isLoading = false
////                                    }
////                                })
////                        } else {
////                            Log.w("Firebase", "Data peminjaman null")
////                            isLoading = false
////                        }
////                    } catch (e: Exception) {
////                        Log.e("Firebase", "Error parsing peminjaman: ${e.message}")
////                        isLoading = false
////                    }
////                }
////
////                override fun onCancelled(error: DatabaseError) {
////                    Log.e("Firebase", "Gagal ambil data peminjaman: ${error.message}")
////                    isLoading = false
////                }
////            })
////    }
//
//    LaunchedEffect(borrowId) {
//        isLoading = true
//        databasePeminjaman.child(borrowId).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                peminjaman = snapshot.getValue(Peminjaman::class.java)
//                isLoading = false // Set loading to false after fetching data
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Firebase", "Gagal ambil data: ${error.message}")
//                isLoading = false // Set loading to false even in case of error
//            }
//        })
//    }
//
//
//    Scaffold(
//        bottomBar = { Footer(navController = navController) }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            } else {
//                if (peminjaman != null) {
//                    Column(
//                        modifier = Modifier
//                            .verticalScroll(rememberScrollState())
//                            .padding(16.dp)
//                    ) {
//
//
//                        // Detail Barang
////                        Card(
////                            modifier = Modifier.fillMaxWidth(),
////                            elevation = CardDefaults.cardElevation(4.dp)
////                        ) {
////                            Column(modifier = Modifier.padding(16.dp)) {
////                                Text(
////                                    text = barang!!.name,
////                                    fontSize = 20.sp,
////                                    fontWeight = FontWeight.Bold
////                                )
////                                Text(
////                                    text = "Stok Tersisa: ${barang!!.stock}",
////                                    fontSize = 14.sp
////                                )
////                            }
////                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Detail Peminjaman
////                        Card(
////                            modifier = Modifier.fillMaxWidth(),
////                            elevation = CardDefaults.cardElevation(4.dp)
////                        ) {
////                            Column(modifier = Modifier.padding(16.dp)) {
////                                Text(
////                                    text = "Informasi Peminjaman",
////                                    fontSize = 18.sp,
////                                    fontWeight = FontWeight.Bold
////                                )
////                                Spacer(modifier = Modifier.height(8.dp))
////                                Text("Nama Peminjam: ${peminjaman.namaPeminjam}")
////                                Text("Alamat: ${peminjaman.alamatPeminjam}")
////                                Text("Email: ${peminjaman.emailPeminjam}")
////                                Text("No HP: ${peminjaman.phoneNumberPeminjam}")
////                                Text("Tanggal Pinjam: ${peminjaman!!.tanggalPinjam}")
////                                Text("Tanggal Kembali: ${peminjaman!!.tanggalPengembalian}")
////                                Text("Status: ${peminjaman!!.status}")
////                            }
////                        }
//
//                        peminjaman?.let { item ->
//                            // Show data only after it is fetched
//                            Card(
//                                modifier = Modifier.fillMaxWidth(),
//                                shape = RoundedCornerShape(8.dp),
//                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//                            ) {
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(16.dp),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    // Ikon barang
//                                    Box(
//                                        modifier = Modifier
//                                            .size(48.dp)
//                                            .background(Color.LightGray, CircleShape),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = item.barangid.take(1).uppercase(),
//                                            fontSize = 20.sp,
//                                            fontWeight = FontWeight.Bold,
//                                            color = Color.Gray
//                                        )
//                                    }
//
//                                    Text(
//                                        text = "${borrowId} ini id pemijamannya",
//                                        fontSize = 16.sp,
//                                        fontWeight = FontWeight.Medium
//                                    )
//                                    Spacer(modifier = Modifier.width(16.dp))
//
//                                    // Detail informasi barang
//                                    Column {
//                                        Text(text = item.namaPeminjam, fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                                        Text(text = "Kode Inventaris: ${item.alamatPeminjam}", fontSize = 12.sp, color = Color.Gray)
//                                        Text(text = "Jumlah Tersedia: ${item.emailPeminjam} ", fontSize = 12.sp, color = Color.Gray)
//                                        Text(text = "Kondisi Barang: ${item.phoneNumberPeminjam}", fontSize = 12.sp, color = Color.Gray)
//                                        Text(text = "Lokasi Penyimpanan: ${item.tanggalPinjam}", fontSize = 12.sp, color = Color.Gray)
//                                    }
//                                }
//                            }
//                        } ?: run {
//                            Text("Memuat data barang... ", modifier = Modifier.padding(top = 16.dp))
//                        }
//
//                    }
//                } else {
//                    Text(
//                        text = "Data tidak ditemukan atau rusak.",
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//            }
//        }
//    }
//}


//    Scaffold(
//        bottomBar = {
////            Footer(navController = navController )
//        }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            var username by remember { mutableStateOf("") }
//            var phoneNumber by remember { mutableStateOf("") }
//            var email by remember { mutableStateOf("") }
//            var address by remember { mutableStateOf("") }
//            var isLoading by remember { mutableStateOf(true) }
//
//            var jumlahPinjam by remember { mutableStateOf(0) }
//            var isChecked by remember { mutableStateOf(false) }
//
//            val usernameTemp = remember { mutableStateOf("") }
//            val phoneNumberTemp = remember { mutableStateOf("") }
//            val addressTemp = remember { mutableStateOf("") }
//            val catatanTemp = remember { mutableStateOf("") }
//
//
//            val tanggalPinjam = remember { mutableStateOf("") }
//            val tanggalKembali = remember { mutableStateOf("") }
//
//            val currentUser = FirebaseAuth.getInstance().currentUser
//            val uid = currentUser?.uid
//            val context = LocalContext.current
//
//            // Listener untuk pembaruan data secara real-time
//            DisposableEffect(uid) {
//                if (uid != null) {
//                    val database = FirebaseDatabase.getInstance()
//                    val userRef = database.getReference("users").child(uid)
//
//                    val listener = object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            snapshot.let {
//                                username = it.child("name").getValue(String::class.java) ?: ""
//                                email = it.child("email").getValue(String::class.java) ?: ""
//                                address = it.child("address").getValue(String::class.java) ?: ""
//                                phoneNumber =
//                                    it.child("phoneNumber").getValue(String::class.java) ?: ""
//                            }
//                            isLoading = false
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast.makeText(
//                                context, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT
//                            ).show()
//                            isLoading = false
//                        }
//                    }
//
//                    userRef.addValueEventListener(listener)
//
//                    // Hapus listener saat composable dihancurkan
//                    onDispose {
//                        userRef.removeEventListener(listener)
//                    }
//                } else {
//                    navController.navigate("Login")
//                }
//
//                onDispose { } // Dibutuhkan oleh DisposableEffect meskipun tidak ada tambahan logika
//            }
//
//            LaunchedEffect(username, phoneNumber, address) {
//                usernameTemp.value = username
//                phoneNumberTemp.value = phoneNumber
//                addressTemp.value = address
//            }
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(16.dp)
//                    .padding(bottom = 10.dp)
//                    .background(Color.White)
//            ) {
//                // Header dengan tombol kembali dan judul
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { /* Handle back */ }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                    Column(
//                        modifier = Modifier
//                            .padding(10.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = "Nama Barang",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        androidx.compose.material3.Text(
//                            text = "Kategori barang",
//                            fontSize = 14.sp,
//                            color = Color.Gray
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                if (isLoading) {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//                }
//
//                // Informasi Barang
//                barang?.let { item ->
//                    // Show data only after it is fetched
//                    DetailBarang(item)
//                } ?: run {
//                    androidx.compose.material3.Text(
//                        "Memuat data barang... ",
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Pertanyaan Peminjaman
//                androidx.compose.material3.Text(
//                    text = "Ingin pinjam barang?",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                androidx.compose.material3.Text(
//                    text = "Silahkan isi form dibawah ini",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//
//                Column(modifier = Modifier.padding(16.dp)) {
//                    FormFieldPinjam(label = "Nama", "Fulan", usernameTemp)
//                    FormFieldPinjam(label = "Alamat", "Jalan Veteran No 1", addressTemp)
//                    FormFieldPinjam(label = "Nomor Handphone", "081234567890", phoneNumberTemp)
//                    //add jumlah
//                    barang?.let { item ->
//                        androidx.compose.material3.Text(
//                            text = "Jumlah Peminjaman",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Medium,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//
//                        Column(
//                            verticalArrangement = Arrangement.Center,
//                            modifier = Modifier
////                                .fillMaxWidth()
//                                .height(48.dp) // Sesuaikan tinggi agar sejajar
//                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
//                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
//                                .padding(horizontal = 4.dp)
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//
//                                ) {
//                                IconButton(
//                                    onClick = {
//                                        if (jumlahPinjam > 1) jumlahPinjam--
//                                    }
//                                ) {
//                                    Icon(
//                                        Icons.Default.Close,
//                                        contentDescription = "Kurangi"
//                                    ) //sementara
//                                }
//
//                                androidx.compose.material3.Text(
//                                    text = jumlahPinjam.toString(),
//                                    modifier = Modifier.padding(horizontal = 16.dp),
//                                    fontSize = 16.sp
//                                )
//
//                                IconButton(
//                                    onClick = {
//                                        if (jumlahPinjam < item.stock) jumlahPinjam++
//                                    }
//                                ) {
//                                    Icon(Icons.Default.Add, contentDescription = "Tambah")
//                                }
//
//                                Spacer(modifier = Modifier.width(6.dp))
//
//                                if (jumlahPinjam == item.stock){
//                                    androidx.compose.material3.Text(
//                                        text = " |      Maks: ${item.stock}",
//                                        color = Color.Red,
//                                        fontSize = 14.sp
//                                    )
//                                    Spacer(modifier = Modifier.width(6.dp))
//                                }
//
//                            }
//
//
//                        }
//                    }
//                    FormFieldTanggal(label = "Tanggal Peminjaman", valueState = tanggalPinjam)
//                    FormFieldTanggal(label = "Tanggal Pengembalian", valueState = tanggalKembali)
//
//
//
//                    //add catatan
//                    val catatan = remember { mutableStateOf("") }
//
//                    FormFieldCatatan(
//                        label = "Catatan",
//                        placeholder = "Masukkan catatan tambahan...",
//                        valueState = catatan
//                    )
//
//
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Checkbox
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Checkbox(
//                        checked = isChecked,
//                        onCheckedChange = { isChecked = it }
//                    )
//
//                    androidx.compose.material3.Text(
//                        text = "Saya setuju dengan peraturan peminjaman barang dari masjid",
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Tombol Ajukan Peminjaman
//                Button(
//                    onClick = {
//                        val uid = FirebaseAuth.getInstance().currentUser?.uid
//                        if (uid != null) {
//                            val database = FirebaseDatabase.getInstance()
//                            val borrowRef = database.getReference("peminjaman").push()
//
//                            val tanggalPengajuan = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
//                            val timestamp = System.currentTimeMillis()
//
//                            val borrowData = mapOf(
//                                "uid" to uid,
//                                "barangid" to itemId,
//                                "namaPeminjam" to usernameTemp.value,
//                                "alamatPeminjam" to addressTemp.value,
//                                "emailPeminjam" to email,
//                                "phoneNumberPeminjam" to phoneNumberTemp.value,
//                                "tanggalPinjam" to tanggalPinjam.value,
//                                "tanggalPengembalian" to tanggalKembali.value,
//                                "tanggalPengajuan" to tanggalPengajuan,
//                                "timestamp" to timestamp,
//                                "status" to "diajukan"
//                            )
//
//                            borrowRef.setValue(borrowData)
//                                .addOnSuccessListener {
//                                    navController.navigate("suksesPinjam")
//                                }
//                                .addOnFailureListener { error ->
//                                    Toast.makeText(context, "Gagal menyimpan data: ${error.message}", Toast.LENGTH_SHORT).show()
//                                }
//                        } else {
//                            Toast.makeText(context, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
//                        }
//                    },
//
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
//                ) {
//                    androidx.compose.material3.Text(
//                        text = "Ajukan Peminjaman",
//                        fontSize = 16.sp,
//                        color = Color.White
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun PeminjamanScreen(
//    navController: NavController,
//    viewModel: PeminjamanViewModel
//) {
//    val peminjamanList by viewModel.peminjamanList..observeAsState(initialValue = emptyList())
//    val error by viewModel.error.observeAsState(initialValue = null)
//    var isLoading by remember { mutableStateOf(true) }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        viewModel.fetchPeminjaman()
//    }
//
//    LaunchedEffect(peminjamanList) {
//        if (peminjamanList.isNotEmpty()) {
//            isLoading = false
//        }
//    }
//
//    Scaffold(
//        bottomBar = {
//            // Footer(navController = navController)
//        }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//            ) {
//                // Header
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back"
//                        )
//                    }
//                    Text(
//                        text = "Daftar Peminjaman",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
//
//                if (isLoading) {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//                } else if (peminjamanList.isEmpty()) {
//                    Text(
//                        text = "Tidak ada data peminjaman.",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        color = Color.Gray,
//                        textAlign = TextAlign.Center
//                    )
//                } else {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 16.dp)
//                    ) {
//                        items(peminjamanList) { peminjaman ->
//                            PeminjamanCard(
//                                peminjaman = peminjaman,
//                                onApprove = {
//                                    coroutineScope.launch {
//                                        viewModel.updateStatus(peminjaman.id, "disetujui")
//                                        snackbarHostState.showSnackbar("Peminjaman disetujui!")
//                                    }
//                                },
//                                onReject = {
//                                    coroutineScope.launch {
//                                        viewModel.updateStatus(peminjaman.id, "ditolak")
//                                        snackbarHostState.showSnackbar("Peminjaman ditolak!")
//                                    }
//                                }
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                        }
//                    }
//                }
//            }
//
//            // Snackbar for errors
//            error?.let { errorMessage ->
//                LaunchedEffect(errorMessage) {
//                    snackbarHostState.showSnackbar(errorMessage)
//                }
//            }
//        }
//
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}

@Composable
fun DetailBorrowScreen(
    navController: NavController,
    borrowId: String
) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance()
    val databaseBarang = database.getReference("barang")
    val databasePeminjaman = database.getReference("peminjaman")

    var peminjaman by remember { mutableStateOf<Peminjaman?>(null) }
    var barang by remember { mutableStateOf<Barang?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var itemId by remember { mutableStateOf("") }

    LaunchedEffect(borrowId) {
        isLoading = true

        databasePeminjaman.child(borrowId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val peminjamanData = snapshot.getValue(Peminjaman::class.java)
                        if (peminjamanData != null) {
                            peminjaman = peminjamanData
                            itemId = peminjamanData.barangid

                            // Ambil data barang berdasarkan itemId
                            databaseBarang.child(itemId)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(barangSnapshot: DataSnapshot) {
                                        barang = barangSnapshot.getValue(Barang::class.java)
                                        isLoading = false
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("Firebase", "Gagal ambil data barang: ${error.message}")
                                        isLoading = false
                                    }
                                })
                        } else {
                            Log.w("Firebase", "Data peminjaman null")
                            isLoading = false
                        }
                    } catch (e: Exception) {
                        Log.e("Firebase", "Error parsing peminjaman: ${e.message}")
                        isLoading = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Gagal ambil data peminjaman: ${error.message}")
                    isLoading = false
                }
            })
    }


    Scaffold(
        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                peminjaman?.let { data ->
                        // Detail Barang
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            // Title and Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Peminjaman oleh ${data.namaPeminjam}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.status.replaceFirstChar { it.uppercase() },
                                    fontSize = 14.sp,
                                    color = when (data.status.lowercase()) {
                                        "diajukan" -> Color(0xFF34A853) // Green for pending
                                        "disetujui" -> Color.Blue // Blue for approved
                                        "ditolak" -> Color.Red // Red for rejected
                                        else -> Color.Gray
                                    },
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Details
                            Text(
                                text = "Email: ${data.emailPeminjam}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Telepon: ${data.phoneNumberPeminjam}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Alamat: ${data.alamatPeminjam}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tanggal Pengajuan: ${data.tanggalPengajuan}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Tanggal Pinjam: ${data.tanggalPinjam}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Tanggal Kembali: ${data.tanggalPengembalian}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        }
                }
            }
        }
    }


//@Composable
//fun PeminjamanCard(
//    peminjaman: Peminjaman,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
//            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
//            .padding(16.dp)
//    ) {
//        // Title and Status
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Peminjaman oleh ${peminjaman.namaPeminjam}",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = peminjaman.status.replaceFirstChar { it.uppercase() },
//                fontSize = 14.sp,
//                color = when (peminjaman.status.lowercase()) {
//                    "diajukan" -> Color(0xFF34A853) // Green for pending
//                    "disetujui" -> Color.Blue // Blue for approved
//                    "ditolak" -> Color.Red // Red for rejected
//                    else -> Color.Gray
//                },
//                fontWeight = FontWeight.Medium
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Details
//        Text(text = "ID Barang: ${peminjaman.barangid}", fontSize = 14.sp, color = Color.Gray)
//        Text(text = "Email: ${peminjaman.emailPeminjam}", fontSize = 14.sp, color = Color.Gray)
//        Text(text = "Telepon: ${peminjaman.phoneNumberPeminjam}", fontSize = 14.sp, color = Color.Gray)
//        Text(text = "Alamat: ${peminjaman.alamatPeminjam}", fontSize = 14.sp, color = Color.Gray)
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(text = "Tanggal Pengajuan: ${peminjaman.tanggalPengajuan}", fontSize = 14.sp, color = Color.Gray)
//        Text(text = "Tanggal Pinjam: ${peminjaman.tanggalPinjam}", fontSize = 14.sp, color = Color.Gray)
//        Text(text = "Tanggal Kembali: ${peminjaman.tanggalPengembalian}", fontSize = 14.sp, color = Color.Gray)
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Action Buttons (for admin view)
////        if (peminjaman.status.lowercase() == "diajukan") {
////            Row(
////                modifier = Modifier.fillMaxWidth(),
////                horizontalArrangement = Arrangement.SpaceBetween
////            ) {
////                Button(
////                    onClick = onApprove,
////                    modifier = Modifier
////                        .weight(1f)
////                        .height(40.dp),
////                    shape = RoundedCornerShape(8.dp),
////                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
////                ) {
////                    Text(text = "Setujui", fontSize = 14.sp, color = Color.White)
////                }
////                Spacer(modifier = Modifier.width(8.dp))
////                Button(
////                    onClick = onReject,
////                    modifier = Modifier
////                        .weight(1f)
////                        .height(40.dp),
////                    shape = RoundedCornerShape(8.dp),
////                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
////                ) {
////                    Text(text = "Tolak", fontSize = 14.sp, color = Color.White)
////                }
////            }
////        }
//    }
//}

@Composable
fun ReturnScreen(navController: NavController) {
    var jumlahBarang by remember { mutableStateOf("5") }
    var tanggalPinjam by remember { mutableStateOf("15/03/2025") }
    var tanggalPengembalian by remember { mutableStateOf("18/03/2025") }
    var catatan by remember { mutableStateOf("Khataman mingguan") }

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
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "Masjid Raden Patah",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "Lowokwaru, Malang",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Borrowing Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = "Waktu Pengajuan",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "14 Maret 2025, 09:39",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "Waktu Peminjaman",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "15 Maret 2025",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Estimasi Pengembalian",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "21 Maret 2025",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Title
        Text(
            text = "Rincian Peminjaman",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Icon Placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack, // Placeholder, replace with custom icon if needed
                contentDescription = "Rincian Icon",
                tint = Color.Green,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        Text(
            text = "Nama Barang*",
            fontSize = 14.sp,
            color = Color.Red
        )
        Text(
            text = "Al-Quran",
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Jumlah Barang*",
            fontSize = 14.sp,
            color = Color.Red
        )
        Text(
            text = jumlahBarang,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tanggal Pinjam*",
            fontSize = 14.sp,
            color = Color.Red
        )
        Text(
            text = tanggalPinjam,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tanggal Pengembalian*",
            fontSize = 14.sp,
            color = Color.Red
        )
        Text(
            text = tanggalPengembalian,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Catatan*",
            fontSize = 14.sp,
            color = Color.Red
        )
        Text(
            text = catatan,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = { /* Handle submission */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
        ) {
            Text(
                text = "Ajukan Pengembalian",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ReturnPreview(){
    ReturnScreen(navController = rememberNavController())
}