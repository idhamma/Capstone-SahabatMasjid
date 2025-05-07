package com.sepertigamalamdev.sahabatmasjid.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.sepertigamalamdev.sahabatmasjid.auth.LoginScreen
import com.sepertigamalamdev.sahabatmasjid.auth.SignUpScreen

@Composable
fun LandingScreen(
    onPreSignUp: () -> Unit,
    onPreLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A3C34)) // Dark green background as in the first image
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(8f))

        // Greeting Text
        Text(
            text = "Selamat Datang Di SahabatMasjid!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Register Button
        Button(
            onClick = onPreSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Daftar Akun")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Link
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sudah Punya Akun? ",
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = "Masuk",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.clickable { onPreLogin() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen (
                onPreSignUp = { navController.navigate("preSignUp") },
                onPreLogin = { navController.navigate("preLogin") },
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("signUp") }
                
            )
        }
        composable("preLogin") {
            preLoginScreen(
                navController = navController,
                onLoginClick = { navController.navigate("login") }
            )
        }
        composable("preSignUp") {
            preSignUpScreen(
                navController = navController,
                onRegisterClick = { navController.navigate("signUp") }
            )
        }
        composable("login") { LoginScreen(navController = navController) }
        composable("signUp") { SignUpScreen(navController = navController) }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun LandingScreenPreview() {
//    LandingScreen(
//        onRegisterClick = { },
//        onLoginClick = { }
//    )
//}

@Preview(showBackground = true)
@Composable
fun AppNavigationDebugPreview() {
    AppNavigation()
}