package com.sepertigamalamdev.sahabatmasjid.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

//import com.sepertigamalamdev.sahabatmasjid.

//@Composable
//fun SignUpScreen(navController: NavController) {
//    Scaffold(
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color.White)
//        ) {
//
//            SignUp(navController = navController)
//
//        }
//    }
//}
//@Composable
//fun SignUp(navController: NavController) {
//    // State for input fields
//    var fullName by remember { mutableStateOf("") }
//    var phoneNumber by remember { mutableStateOf("") }
//    var address by remember { mutableStateOf("") }
//    var positionStatus by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp)
//            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Title
//        Spacer(modifier = Modifier.weight(0.1f))
//
//        Row (modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically){
//
//            BackIcon(navController = navController)
//
//            Text(
//                text = "Daftar Akun",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = " ",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Full Name Field
//        OutlinedTextField(
//            value = fullName,
//            onValueChange = { fullName = it },
//            label = { Text("Nama Lengkap") },
//            placeholder = { Text("Fulan") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Phone Number Field
//        OutlinedTextField(
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            label = { Text("Nomor HP") },
//            placeholder = { Text("08123456789") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        // Phone Number Field
//        OutlinedTextField(
//            value = phoneNumber,
//            onValueChange = { phoneNumber = it },
//            label = { Text("Email") },
//            placeholder = { Text("masjid@gmail.com") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Address Field
//        OutlinedTextField(
//            value = address,
//            onValueChange = { address = it },
//            label = { Text("Alamat") },
//            placeholder = { Text("Jalan Raya No. 4") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Register Button
//        Button(
//            onClick = {}, // Empty since no functionality
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color(0xFF34A853),
//                contentColor = Color.White
//            )
//        ) {
//            Text("Daftar")
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
//@Composable
//fun BackIcon(navController: NavController) {
//    IconButton(
//        onClick = {
//            navController.navigate("landing") // Changed to "welcome" to match your route
//        }
//    ) {
//        Icon(
//            imageVector = Icons.Filled.ArrowBack,
//            contentDescription = "Back",
//            modifier = Modifier.size(24.dp),
//            tint = Color.Black
//        )
//    }
//}
//
////@Preview(showBackground = true)
//@Preview
//@Composable
//fun SignUpPreview(){
//    val navController = rememberNavController()
//    SignUpScreen(navController = navController)
//}
@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference

    fun validateInputs(): Boolean {
        return when {
            name.isBlank() -> {
                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            email.isBlank() -> {
                Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            password.isBlank() || confirmPassword.isBlank() -> {
                Toast.makeText(context, "Password fields cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            !isChecked -> {
                Toast.makeText(context, "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    fun saveUserDataToDatabase(userId: String, name: String, email: String) {
        val user = mapOf(
            "name" to name,
            "email" to email,
            "phoneNumber" to "",
            "address" to "",
            "photoUrl" to "",
        )
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                navController.navigate("Masuk")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tombol back
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        Text(
            text = "Daftar",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Buat akun untuk memulai",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Alamat Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Buat Kata Sandi") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Kata Sandi") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row {
                    Text(
                        text = "Saya sudah menyetujui ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Syarat dan Ketentuan",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }
                Row {
                    Text(
                        text = "beserta dengan ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Kebijakan Privasi",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }
            }
        }

        // Tambahan untuk "Sudah punya akun? Login"
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sudah punya akun? ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate("Login")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validateInputs()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                auth.currentUser?.uid?.let { userId ->
                                    saveUserDataToDatabase(userId, name, email)
                                } ?: run {
                                    Toast.makeText(
                                        context,
                                        "Failed to retrieve user information",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Signup failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("Daftar", fontSize = 16.sp)
        }
    }
}
