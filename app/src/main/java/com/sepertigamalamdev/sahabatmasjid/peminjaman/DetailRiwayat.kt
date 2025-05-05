package com.sepertigamalamdev.sahabatmasjid.peminjaman



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailRiwayat2() {
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
                    text = "Waktu Pengembalian",
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

@Preview(showBackground = true)
@Composable
fun DetailRiwayatPreview(){
    DetailRiwayat2()
}