package com.sepertigamalamdev.sahabatmasjid.peminjaman


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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

@Preview(showBackground = true)
@Composable
fun DetailPeminjamanPreview(){
    val navController = NavController(LocalContext.current)
    DetailBorrowScreen(navController = navController, borrowId = "123")
}

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
                if (barang != null && peminjaman != null) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Detail Barang
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = barang!!.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Stok Tersisa: ${barang!!.stock}",
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detail Peminjaman
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Informasi Peminjaman",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Nama Peminjam: ${peminjaman!!.namaPeminjam}")
                                Text("Alamat: ${peminjaman!!.alamatPeminjam}")
                                Text("Email: ${peminjaman!!.emailPeminjam}")
                                Text("No HP: ${peminjaman!!.phoneNumberPeminjam}")
                                Text("Tanggal Pinjam: ${peminjaman!!.tanggalPinjam}")
                                Text("Tanggal Kembali: ${peminjaman!!.tanggalPengembalian}")
                                Text("Status: ${peminjaman!!.status}")
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Data tidak ditemukan atau rusak.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}


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


