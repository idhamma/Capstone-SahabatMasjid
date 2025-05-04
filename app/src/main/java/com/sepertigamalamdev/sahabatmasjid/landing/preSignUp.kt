package com.sepertigamalamdev.sahabatmasjid.landing


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun preSignUpScreen(navController: NavController, onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Icon and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon(navController = navController)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Daftar Akun",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(24.dp)) // Balance the row
        }

        Spacer(modifier = Modifier.weight(1f))

        // Via Phone Number Button
        Button(
            onClick = { /* Handle phone number sign-up */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF34A853), // Google green color
                contentColor = Color.White
            )
        ) {
            Text("Lewat Nomor HP")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider Text
        Text(
            text = "atau",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Via Google Button
        Button(
            onClick = { /* Handle Google sign-up */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Lewat Google")
        }

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

//@Preview(showBackground = true)
//@Composable
//fun preSignUpScreenPreview() {
//    preSignUpScreen(navController = rememberNavController()) { navController.navigate("signUp") }
//}