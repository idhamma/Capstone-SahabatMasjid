package com.sepertigamalamdev.sahabatmasjid.login

import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginScreen(navController: NavController ){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(0.1f))
        // Title
        Text(
            text = "Masuk ke Akun",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Nomor Handphone") },
            placeholder = { Text("08123456789") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password") },
            placeholder = { Text("Enter password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock    ,
                    contentDescription = "Password visibility icon"
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {}, // Empty since no functionality
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF34A853), // Google green color
                contentColor = Color.White
            )
        ) {
            Text("Masuk")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password Link
        Text(
            text = "Lupa Password?",
            color = Color.Blue,
            fontSize = 14.sp
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


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}