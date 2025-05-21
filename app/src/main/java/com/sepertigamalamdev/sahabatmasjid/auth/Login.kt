package com.sepertigamalamdev.sahabatmasjid.auth

import android.app.Activity
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

//@Composable
//fun LoginScreen(navController: NavController) {
//    Scaffold(
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//
//            Login(navController = navController)
//
//        }
//    }
//}
//
//
//@Composable
//fun Login(navController: NavController ){
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp)
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        Spacer(modifier = Modifier.weight(0.1f))
//        // Title
//        Text(
//            text = "Masuk ke Akun",
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Email Field
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Nomor Handphone") },
//            placeholder = { Text("08123456789") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Password Field
//        OutlinedTextField(
//            value = password,
//            onValueChange = {password = it},
//            label = { Text("Password") },
//            placeholder = { Text("Enter password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock    ,
//                    contentDescription = "Password visibility icon"
//                )
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Login Button
//        Button(
//            onClick = {navController.navigate("homepage")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color(0xFF34A853), // Google green color
//                contentColor = Color.White
//            )
//        ) {
//            Text("Masuk")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Forgot Password Link
//        Text(
//            text = "Lupa Password?",
//            color = Color.Blue,
//            fontSize = 14.sp
//        )
//
//        Row {
//            Text(
//                text = "Belum punya akun?",
//                color = Color.Blue,
//                fontSize = 14.sp
//            )
//
//            Text(
//                text = "Daftar",
//                color = Color.Blue,
//                fontSize = 14.sp,
//                modifier = Modifier.clickable {
//                    navController.navigate("signup")
//                }
//            )
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Terms and Privacy Policy
//        Text(
//            text = "By using Sahabat Masjid, you agree to the Terms and Privacy Policy.",
//            fontSize = 12.sp,
//            color = Color.Gray,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview(){
//    val navController = rememberNavController()
//    Login(navController = navController)
//}


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    // Handle the back press
    BackHandler {
        // Close the app instead of navigating back
        (context as? Activity)?.finish()
    }
    // States
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo
//        Image(
////            painter = painterResource(id = R.drawable.laundrygo2_1),
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = "App Logo",
//            modifier = Modifier.size(150.dp)
//        )

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.Text(
            text = "Selamat Datang!",
            fontSize = 24.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        androidx.compose.material3.OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Alamat Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Kata Sandi") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                performLogin(
                    email = email,
                    password = password,
                    onSuccess = {
                        navController.navigate("landing") {
                            popUpTo("Login") { inclusive = true }
                        }
                    },
                    onError = { error ->
                        errorMessage = error
                    },
                    onLoading = { loading ->
                        isLoading = loading
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Gray, // Warna tombol latar belakang
                contentColor = Color.White     // Warna teks tombol
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White, // Warna indikator loading
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Masuk")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Registration Prompt
        val annotatedText = buildAnnotatedString {
            append("Belum punya akun? ")
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append("Daftar Sekarang")
            }
        }

        ClickableText(
            text = annotatedText,
            onClick = { navController.navigate("Signup") },
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}

fun performLogin(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
    onLoading: (Boolean) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onError("Silahkan isi email dan kata sandi dahulu")
        return
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onError("Format email salah")
        return
    }

    onLoading(true)
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onLoading(false)
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMessage = when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Kata sandi salah"
                    is FirebaseAuthInvalidUserException -> "Pengguna belum terdaftar"
                    else -> "Gagal masuk. Silahkan coba lagi"
                }
                onError(errorMessage)
            }
        }
}