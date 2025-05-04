package com.sepertigamalamdev.sahabatmasjid.homepage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomepageScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
//            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Greeting
        Text(
            text = "Assalamualaikum, Jamaah",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = { /* Static for now */ },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text("Cari Inventaris") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.MoreVert, //sementara
                    contentDescription = "Filter",
                    tint = Color.Gray
                )
            },
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Image Placeholder",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Status Section
        Text(
            text = "INFO STATUS",
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A3C34), RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Tersedia
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Tersedia",
                    tint = Color.Green,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tersedia",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "25 Barang",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            // Dipinjam
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AddCircle, //sementara
                    contentDescription = "Dipinjam",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dipinjam",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "5 Barang",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Riwayat Section
        Text(
            text = "Riwayat",
            fontSize = 18.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Mic History
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            backgroundColor = Color.LightGray,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info, //sementara
                    contentDescription = "Mic",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "MIC",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Dipinjam",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "01/04/2025 - 02/04/2025",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Sound System History
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.LightGray,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info, //sementara
                    contentDescription = "Sound System",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "SOUND SYSTEM",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Dikembalikan",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "25/03/2025 - 27/03/2025",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Footer()
    }



}

@Preview(showBackground = true)
@Composable
fun HomepageScreenPreview() {
    HomepageScreen()
}