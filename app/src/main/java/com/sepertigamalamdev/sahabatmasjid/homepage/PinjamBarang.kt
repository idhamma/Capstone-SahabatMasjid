package com.sepertigamalamdev.sahabatmasjid.homepage


import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BorrowScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
//            .padding(bottom = 10.dp)
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
            Column (
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
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

        // Informasi Barang
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Item Icon",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "Mic",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Kode Inventaris: INV-01",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Jumlah Tersedia: 5 unit dari 10 total",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Kondisi Barang: Baik",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Lokasi Penyimpanan: Ruang Audio",
            fontSize = 14.sp,
            color = Color.Gray
        )

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

        // Form Peminjaman
        FormField(label = "Nama", placeholder = "Fulan bin Afwan")
        FormField(label = "Nomor telepon", placeholder = "08123456789")
        FormField(label = "Jumlah barang", placeholder = "6")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormField(label = "Tanggal peminjaman", placeholder = "DD/MM/YYYY")
            IconButton(onClick = { /* Handle calendar */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, //sementara
                    contentDescription = "Calendar",
                    tint = Color.Green
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormField(label = "Tanggal pengembalian", placeholder = "DD/MM/YYYY")
            IconButton(onClick = { /* Handle calendar */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,//sementara
                    contentDescription = "Calendar",
                    tint = Color.Green
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = { /* Handle checkbox */ }
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
            onClick = { /* Handle click */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Ajukan Peminjaman", fontSize = 16.sp, color = Color.White)
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
                if (true) { // Placeholder condition
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
fun BorrowScreenPreview(){
    BorrowScreen()
}