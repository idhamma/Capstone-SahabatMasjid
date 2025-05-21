package com.sepertigamalamdev.sahabatmasjid.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.compose.runtime.getValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sepertigamalamdev.sahabatmasjid.management.UserViewModel.UserProfileViewModel
import java.lang.System

@Composable
fun ConfirmDataScreen(
    userViewModel: UserProfileViewModel = viewModel(),
    masjidViewModel: MasjidViewModel = viewModel(),
    navController: NavController,
    masjidid: String
){
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance()
    val refRequest = database.getReference("RoleRequest")

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    if (FirebaseAuth.getInstance().currentUser == null) {
        LaunchedEffect(Unit) {
            navController.navigate("Login")
        }
    }

    if (userViewModel.isLoading) {
        CircularProgressIndicator()
    } else {

        LaunchedEffect(masjidid) {
            masjidViewModel.fetchMasjidData(masjidid)
        }

        val masjid by masjidViewModel.masjid

        var isAgreed by remember { mutableStateOf(false) }

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
                        text = masjid.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section Title
                Text(
                    text = "Konfirmasi Data Diri",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pastikan data diri sesuai",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Form Fields
                OutlinedTextField(
                    value = userViewModel.username,
                    onValueChange = { userViewModel.username = it },
                    label = { Text("Nama") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    readOnly = true
                )

                OutlinedTextField(
                    value = userViewModel.phoneNumber,
                    onValueChange = { userViewModel.phoneNumber = it },
                    label = { Text("Nomor telepon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    readOnly = true
                )

                OutlinedTextField(
                    value = userViewModel.email,
                    onValueChange = { userViewModel.email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    readOnly = true
                )

                OutlinedTextField(
                    value = userViewModel.address,
                    onValueChange = { userViewModel.address = it },
                    label = { Text("Alamat") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    readOnly = true
                )

                // Checkbox for Terms
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAgreed,
                        onCheckedChange = { isAgreed = it }
                    )
                    Text(
                        text = "Saya setuju dengan peraturan dan kesepakatan jemaah dari masjid",
                        fontSize = 14.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Submit Button
                Button(
                    onClick = {
                        val key = refRequest.push().key
                        key?.let {
                            val request = RoleRequest(
                                id = it,
                                masjidid = masjidid,
                                uid = uid.toString(),
                                status = "pending",
                                requestedAt = System.currentTimeMillis()

                            )
                            refRequest.child(it).setValue(request)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34A853)),
                    enabled = isAgreed // Enable button only if checkbox is checked
                ) {
                    Text(
                        text = "Ajukan menjadi Jemaah",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

}

