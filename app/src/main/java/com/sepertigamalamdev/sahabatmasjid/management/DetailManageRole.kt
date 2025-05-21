package com.sepertigamalamdev.sahabatmasjid.management


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer
import com.sepertigamalamdev.sahabatmasjid.management.Barang
import com.google.firebase.database.DatabaseReference


import com.sepertigamalamdev.sahabatmasjid.management.User as AppUser


@Composable
fun DetailManageRoleScreen(navController: NavController, itemId: String) {
    val context = LocalContext.current
    val reqDatabase = FirebaseDatabase.getInstance().getReference("RoleRequest")
    var request by remember { mutableStateOf< RoleRequest?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val roleRef = FirebaseDatabase.getInstance().getReference("role")
    val archiveRef = FirebaseDatabase.getInstance().getReference("RoleRequestArchive")

    val userViewModel: UserViewModel = viewModel()
    var user by remember { mutableStateOf<AppUser?>(null) }

    // Ambil data berdasarkan ID
    LaunchedEffect(itemId) {
        reqDatabase.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                request = snapshot.getValue(RoleRequest::class.java)
                isLoading = false // Set loading to false after fetching data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Gagal ambil data: ${error.message}")
                isLoading = false // Set loading to false even in case of error
            }
        })
    }

    LaunchedEffect(request?.uid) {
        request?.uid?.let { uid ->
            userViewModel.getUserByUid(uid) { fetchedUser ->
                user = fetchedUser
            }
        }
    }

    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(            modifier = Modifier
                    .fillMaxSize())
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "Manage Jemaah",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }



                    user?.let { data ->

                        Column(modifier = Modifier.padding(16.dp)) {
                            ReadOnlyField(label = "Nama", value = data.name)
                            ReadOnlyField(label = "Nomor Telepon", value = data.phoneNumber)
                            ReadOnlyField(label = "Email", value = data.email)
                            ReadOnlyField(label = "Alamat", value = data.address)

                            Spacer(modifier = Modifier.height(8.dp))

                            Spacer(modifier = Modifier.height(16.dp))



                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Tombol Terima
                                Button(
                                    onClick = {
                                        request?.let {
                                            acceptRequest(it, roleRef, archiveRef, reqDatabase)
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Terima", color = Color.White)
                                }

                                // Tombol Tolak
                                Button(
                                    onClick = {
                                        request?.let { safeRequest ->
                                            declineRequest(
                                                request = safeRequest,
                                                reqDatabase = reqDatabase,
                                                archiveRef = archiveRef,
                                                onComplete = {
                                                    Toast.makeText(context, "Request ditolak", Toast.LENGTH_SHORT).show()
                                                    navController.popBackStack()
                                                },
                                                onError = { errorMsg ->
                                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                                }
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Tolak", color = Color.White)
                                }

                            }


                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ReadOnlyField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = if (value.isEmpty()) "-" else value,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

//@Composable
fun acceptRequest(request: RoleRequest, roleRef: DatabaseReference, archiveRef: DatabaseReference, reqRef: DatabaseReference) {
    val key = roleRef.push().key ?: return
    val role = Role(
        id = key,
        uid = request.uid,
        masjidid = request.masjidid,
        status = "jemaah",
        uid_masjidid = "${request.uid}-${request.masjidid}"
    )
    roleRef.child(key).setValue(role)
    archiveRef.child(request.id).setValue(request)
    reqRef.child(request.id).removeValue()
}

fun declineRequest(
    request: RoleRequest,
    reqDatabase: DatabaseReference,
    archiveRef: DatabaseReference,
    onComplete: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null
) {
    val requestId = request.id

    // Pindahkan ke archive
    archiveRef.child(requestId).setValue(request)
        .addOnSuccessListener {
            // Hapus dari tabel utama setelah berhasil di-arsipkan
            reqDatabase.child(requestId).removeValue()
                .addOnSuccessListener {
                    onComplete?.invoke()
                }
                .addOnFailureListener { error ->
                    onError?.invoke("Gagal menghapus data: ${error.message}")
                }
        }
        .addOnFailureListener { error ->
            onError?.invoke("Gagal mengarsipkan data: ${error.message}")
        }
}
