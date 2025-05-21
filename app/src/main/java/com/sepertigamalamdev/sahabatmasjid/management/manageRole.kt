package com.sepertigamalamdev.sahabatmasjid.management

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.SpacedBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sepertigamalamdev.sahabatmasjid.management.User
import androidx.compose.foundation.lazy.items
import androidx.navigation.compose.NavHost


import com.sepertigamalamdev.sahabatmasjid.management.User as AppUser

// Dummy data class for jemaah


// Dummy list of jemaah


@Composable
fun ManageRequestScreen(navController: NavController, masjidid: String) {
    val database = FirebaseDatabase.getInstance().getReference("RoleRequest")
    var jemaahList by remember { mutableStateOf<List<RoleRequest>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(masjidid) {
        val query = database.orderByChild("masjidid").equalTo(masjidid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<RoleRequest>()
                for (childSnapshot in snapshot.children) {
                    val request = childSnapshot.getValue(RoleRequest::class.java)
                    if (request != null && request.masjidid == masjidid) {
                        list.add(request)
                    }
                }
                jemaahList = list
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching request: ${error.message}")
                isLoading = false
            }
        })
    }

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

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (jemaahList.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Tidak ada permintaan menjadi jemaah untuk masjid ini.", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = jemaahList) { jemaah ->
                    RequestCard(roleRequest = jemaah,navController = navController)
                }
            }
        }
    }
}


@Composable
fun RequestCard(roleRequest: RoleRequest, userViewModel: UserViewModel = viewModel(),navController: NavController) {
    var user by remember { mutableStateOf<AppUser?>(null) }

    LaunchedEffect(roleRequest.uid) {
        userViewModel.getUserByUid(roleRequest.uid) { fetchedUser ->
            user = fetchedUser
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate("manageDetailJemaah/${roleRequest.id}")
            },
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
            user?.let { data ->
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = data.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = data.phoneNumber, fontSize = 14.sp, color = Color.Gray)
                    Text(text = data.email, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Button(
            onClick = { /* Handle accept action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4A017)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Text(text = "Terima", fontSize = 14.sp, color = Color.White)
        }
    }
}



//@Preview()
//@Composable
//fun AcceptJemaahPreview(){
//    ManageRequestScreen(navController = rememberNavController())
//}