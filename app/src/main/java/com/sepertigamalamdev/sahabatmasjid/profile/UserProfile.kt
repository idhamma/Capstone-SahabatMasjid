package com.sepertigamalamdev.sahabatmasjid.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer

@Composable
fun userProfileScreen(navController: NavController){
    Scaffold(
        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            UserProfile(navController = navController)
        }
        }
    }

@Composable
fun UserProfile(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Profile Settings",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Image Placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name
        Text(
            text = "Fulan bin Awan",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name Field
        OutlinedTextField(
            value = "Fulan bin Awan",
            onValueChange = { /* Static for now */ },
            label = { Text("Nama") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Field
        OutlinedTextField(
            value = "083456789",
            onValueChange = { /* Static for now */ },
            label = { Text("Nomor Telepon") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = "pemudatahajud@gmail.com",
            onValueChange = { /* Static for now */ },
            label = { Text("Email") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Address Field
        OutlinedTextField(
            value = "Jln. Raya Tlogomas Gg. 3 No.66, Tlogomas",
            onValueChange = { /* Static for now */ },
            label = { Text("Alamat") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Change Password Option
        Text(
            text = "Ganti Kata Sandi",
            fontSize = 16.sp,
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { /* Static for now */ }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Keluar",
            fontSize = 16.sp,
            color = Color.Red,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { /* Static for now */ }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingsScreenPreview(navController: NavHostController = rememberNavController()) {
    userProfileScreen(navController = navController)
}