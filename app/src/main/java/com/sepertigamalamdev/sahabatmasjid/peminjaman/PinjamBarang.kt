package com.sepertigamalamdev.sahabatmasjid.peminjaman


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.sepertigamalamdev.sahabatmasjid.barang.DetailBarang
import com.sepertigamalamdev.sahabatmasjid.management.Barang
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun BorrowScreen(navController: NavController,itemId :String) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("barang")
    var barang by remember { mutableStateOf<Barang?>(null) }
    var isLoading by remember { mutableStateOf(true) }


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
        bottomBar = {
//            Footer(navController = navController )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            var username by remember { mutableStateOf("") }
            var phoneNumber by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var address by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(true) }

            var jumlahPinjam by remember { mutableStateOf(0) }
            var isChecked by remember { mutableStateOf(false) }

            val usernameTemp = remember { mutableStateOf("") }
            val phoneNumberTemp = remember { mutableStateOf("") }
            val addressTemp = remember { mutableStateOf("") }
            val catatanTemp = remember { mutableStateOf("") }


            val tanggalPinjam = remember { mutableStateOf("") }
            val tanggalKembali = remember { mutableStateOf("") }

            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid
            val context = LocalContext.current

            // Listener untuk pembaruan data secara real-time
            DisposableEffect(uid) {
                if (uid != null) {
                    val database = FirebaseDatabase.getInstance()
                    val userRef = database.getReference("users").child(uid)

                    val listener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.let {
                                username = it.child("name").getValue(String::class.java) ?: ""
                                email = it.child("email").getValue(String::class.java) ?: ""
                                address = it.child("address").getValue(String::class.java) ?: ""
                                phoneNumber =
                                    it.child("phoneNumber").getValue(String::class.java) ?: ""
                            }
                            isLoading = false
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                context, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT
                            ).show()
                            isLoading = false
                        }
                    }

                    userRef.addValueEventListener(listener)

                    onDispose {
                        userRef.removeEventListener(listener)
                    }
                } else {
                    navController.navigate("Login")
                }

                onDispose { }
            }

            LaunchedEffect(username, phoneNumber, address) {
                usernameTemp.value = username
                phoneNumberTemp.value = phoneNumber
                addressTemp.value = address
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(bottom = 10.dp)
                    .background(Color.White)
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
                    Column(
                        modifier = Modifier
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Nama Barang",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kategori barang",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                // Informasi Barang
              barang?.let { item ->
                // Show data only after it is fetched
                DetailBarang(item)
            } ?: run {
                Text("Memuat data barang... ", modifier = Modifier.padding(top = 16.dp))
            }

                Spacer(modifier = Modifier.height(16.dp))

                // Pertanyaan Peminjaman
                Text(
                    text = "Ingin pinjam barang?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Silahkan isi form dibawah ini",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))


                Column(modifier = Modifier.padding(16.dp)) {
                    FormFieldPinjam(label = "Nama", "Fulan", usernameTemp)
                    FormFieldPinjam(label = "Alamat", "Jalan Veteran No 1", addressTemp)
                    FormFieldPinjam(label = "Nomor Handphone", "081234567890", phoneNumberTemp)
                    //add jumlah
                    barang?.let { item ->
                        Text(
                            text = "Jumlah Peminjaman",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
//                                .fillMaxWidth()
                                .height(48.dp) // Sesuaikan tinggi agar sejajar
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(horizontal = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,

                            ) {
                                IconButton(
                                    onClick = {
                                        if (jumlahPinjam > 1) jumlahPinjam--
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Kurangi"
                                    ) //sementara
                                }

                                Text(
                                    text = jumlahPinjam.toString(),
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    fontSize = 16.sp
                                )

                                IconButton(
                                    onClick = {
                                        if (jumlahPinjam < item.stock) jumlahPinjam++
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Tambah")
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                if (jumlahPinjam == item.stock){
                                    Text(
                                        text = " |      Maks: ${item.stock}",
                                        color = Color.Red,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }

                            }


                        }
                    }
                    FormFieldTanggal(label = "Tanggal Peminjaman", valueState = tanggalPinjam)
                    FormFieldTanggal(label = "Tanggal Pengembalian", valueState = tanggalKembali)



                    //add catatan
                    val catatan = remember { mutableStateOf("") }

                    FormFieldCatatan(
                        label = "Catatan",
                        placeholder = "Masukkan catatan tambahan...",
                        valueState = catatan
                    )


                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )

                    Text(
                        text = "Saya setuju dengan peraturan peminjaman barang dari masjid",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Ajukan Peminjaman
                Button(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null && barang != null) {
                            val database = FirebaseDatabase.getInstance()
                            val borrowRef = database.getReference("peminjaman").push()
                            val id = borrowRef.key // Ambil ID unik dari push()

                            val tanggalPengajuan = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                            val timestamp = System.currentTimeMillis()

                            val borrowData = mapOf(
                                "id" to id,
                                "uid" to uid,
                                "barangid" to itemId,
                                "namaPeminjam" to usernameTemp.value,
                                "alamatPeminjam" to addressTemp.value,
                                "emailPeminjam" to email,
                                "phoneNumberPeminjam" to phoneNumberTemp.value,
                                "tanggalPinjam" to tanggalPinjam.value,
                                "tanggalPengembalian" to tanggalKembali.value,
                                "tanggalPengajuan" to tanggalPengajuan,
                                "jumlah" to jumlahPinjam,
                                "timestamp" to timestamp,
                                "status" to "diajukan"
                            )

                            borrowRef.setValue(borrowData)
                                .addOnSuccessListener {
                                    // Kurangi stok setelah data peminjaman berhasil disimpan
                                    val newStock = (barang?.stock ?: 0) - jumlahPinjam
                                    if (newStock >= 0) {
                                        database.getReference("barang").child(itemId).child("stock")
                                            .setValue(newStock)
                                            .addOnSuccessListener {
                                                navController.navigate("suksesPinjam")
                                            }
                                            .addOnFailureListener { error ->
                                                Toast.makeText(
                                                    context,
                                                    "Gagal mengurangi stok: ${error.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Stok tidak mencukupi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(context, "Gagal menyimpan data: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "Pengguna atau data barang tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ,

                            modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853))
                ) {
                    Text(text = "Ajukan Peminjaman", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FormFieldPinjam(label: String, placeholder: String, valueState: MutableState<String>) {
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
                if (valueState.value.isEmpty()) {
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

@Composable
fun FormFieldTanggal(label: String, valueState: MutableState<String>) {
    val context = LocalContext.current

    // DatePicker
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                valueState.value = formatted
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .clickable { datePickerDialog.show() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (valueState.value.isEmpty()) "DD/MM/YYYY" else valueState.value,
                fontSize = 14.sp,
                color = if (valueState.value.isEmpty()) Color.Gray else Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowBack, // ganti dengan Icons.Default.CalendarToday kalau kamu punya
                contentDescription = "Pilih tanggal",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FormFieldCatatan(label: String, placeholder: String, valueState: MutableState<String>) {
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
                .heightIn(min = 120.dp) // Membuat tinggi minimum untuk textarea
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
