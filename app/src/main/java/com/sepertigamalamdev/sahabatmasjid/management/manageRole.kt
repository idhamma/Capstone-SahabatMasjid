package com.sepertigamalamdev.sahabatmasjid.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.SpacedBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Dummy data class for jemaah


// Dummy list of jemaah


@Composable
fun ManageRequestScreen(navController: NavController) {

    val jemaahList = listOf(
        Jemaah("Santoso Bin Fulan", "0888888888", "santoso@gmail.com"),
        Jemaah("Hartanto Bin Fulan", "0888888888", "santoso@gmail.com"),
        Jemaah("Hartono Bin Fulan", "0888888888", "santoso@gmail.com"),
        Jemaah("Joko Bin Fulan", "0888888888", "santoso@gmail.com")
    )


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
                .background(Color(0xFF34A853))
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Penerimaan Jemaah",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // List of Jemaah
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        tint = Color.Green,
                        modifier = Modifier
                            .background(Color(0xFF34A853), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = "Santoso Bin Fulan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "0888888888", fontSize = 14.sp, color = Color.Gray)
                        Text(text = "santosos@gmail.com", fontSize = 14.sp, color = Color.Gray)
                    }
                }
                Button(
                    onClick = { /* Handle accept action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4A017)), // Gold color for "Terima"
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(text = "Terima", fontSize = 14.sp, color = Color.White)
                }
            }
//        }
    }
}


@Preview()
@Composable
fun AcceptJemaahPreview(){
    ManageRequestScreen(navController = rememberNavController())
}