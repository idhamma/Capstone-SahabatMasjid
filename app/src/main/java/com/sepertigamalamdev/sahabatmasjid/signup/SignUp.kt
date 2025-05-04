package com.sepertigamalamdev.sahabatmasjid.signup

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.sepertigamalamdev.sahabatmasjid.landing.LandingScreen
@Composable
fun SignUpScreen(navController: NavController) {
    // State for input fields
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var positionStatus by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Spacer(modifier = Modifier.weight(0.1f))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            BackIcon(navController = navController)

            Text(
                text = "Daftar Akun",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = " ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Full Name Field
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Nama Lengkap") },
            placeholder = { Text("Fulan") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Field
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Nomor HP") },
            placeholder = { Text("08123456789") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Phone Number Field
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Email") },
            placeholder = { Text("masjid@gmail.com") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Address Field
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Alamat") },
            placeholder = { Text("Jalan Raya No. 4") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Position/Status Field
        OutlinedTextField(
            value = positionStatus,
            onValueChange = { positionStatus = it },
            label = { Text("Jabatan/Status") },
            placeholder = { Text("") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {}, // Empty since no functionality
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF34A853),
                contentColor = Color.White
            )
        ) {
            Text("Daftar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password Link
        Text(
            text = "Lupa Password?",
            color = Color.Blue,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Terms and Privacy Policy
        Text(
            text = "By using Sahabat Masjid, you agree to the Terms and Privacy Policy.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun BackIcon(navController: NavController) {
    IconButton(
        onClick = {
            navController.navigate("landing") // Changed to "welcome" to match your route
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview(){
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}
