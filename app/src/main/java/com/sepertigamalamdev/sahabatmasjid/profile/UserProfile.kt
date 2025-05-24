package com.sepertigamalamdev.sahabatmasjid.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
//import androidx.activity.compose.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sepertigamalamdev.sahabatmasjid.homepage.Footer

import coil.compose.SubcomposeAsyncImage
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sepertigamalamdev.sahabatmasjid.management.UserViewModel



@Composable
fun PhotoProfile(
    photoUrl: String,
    username: String, // Untuk inisial jika tidak ada foto
    modifier: Modifier = Modifier,
    onPhotoClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant) // Warna latar belakang jika gambar gagal/kosong
            .clickable { onPhotoClick() },
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNotBlank()) {
            SubcomposeAsyncImage(
                model = photoUrl,
                contentDescription = "Foto Profil $username",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                },
                error = { // Tampilan jika gagal load atau URL tidak valid
                    InitialAvatar(name = username)
                }
            )
        } else {
            // Tampilkan inisial atau ikon placeholder jika tidak ada photoUrl
            InitialAvatar(name = username)
        }
    }
}

@Composable
fun InitialAvatar(name: String, modifier: Modifier = Modifier) {
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.headlineLarge, // Ukuran teks inisial
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        // Atau gunakan Ikon:
        // Icon(
        //     imageVector = Icons.Default.Person,
        //     contentDescription = "Placeholder Foto Profil",
        //     modifier = Modifier.size(80.dp),
        //     tint = MaterialTheme.colorScheme.onSurfaceVariant
        // )
    }
}

@Composable
fun ProfileUser(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController = navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Profile(navController)
        }
    }
}


@Composable
fun Profile(
    navController: NavController,
    // Terima kedua ViewModel
    userViewModel: UserViewModel = viewModel(), // ViewModel luar untuk aksi upload
    userProfileViewModel: UserViewModel.UserProfileViewModel = viewModel() // ViewModel nested untuk display data
) {
    // Ambil data dari UserProfileViewModel
    // State ini akan otomatis update jika listener di UserProfileViewModel aktif
    val username = userProfileViewModel.username
    val nickname = userProfileViewModel.nickname
    val phoneNumber = userProfileViewModel.phoneNumber
    val email = userProfileViewModel.email
    val address = userProfileViewModel.address
    val photoUrl = userProfileViewModel.photoUrl // Ambil photoUrl
    val isLoadingProfile = userProfileViewModel.isLoading

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val context = LocalContext.current

    val fieldMessageMap = mapOf(
        "name" to "Nama",
        "nickname" to "Nama Panggilan",
        "phoneNumber" to "Nomor Handphone",
        "email" to "Email", // Anda memiliki ini, meskipun field email biasanya tidak diedit pengguna setelah dibuat
        "address" to "Alamat"
    )

    // Tidak perlu DisposableEffect di sini lagi jika UserProfileViewModel sudah menanganinya
    // Data akan mengalir dari UserProfileViewModel

    // Image Picker Launcher
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null && currentUser?.uid != null) {
                val currentUid = currentUser.uid
                Log.d("ProfileScreen", "Memulai direct upload untuk UID: $currentUid")
                userViewModel.uploadProfilePhotoDirectly( // Panggil fungsi direct upload
                    userId = currentUid,
                    imageUri = uri,
                    context = context,
                    bucketName = "profiles" // Pastikan nama bucket sesuai
                ) { success, newUrlOrError ->
                    if (success && newUrlOrError != null) {
                        Toast.makeText(context, "Foto profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        // UserProfileViewModel akan otomatis update karena ValueEventListener-nya,
                        // sehingga photoUrl di Profile Composable akan recompose.
                    } else {
                        Toast.makeText(context, "Gagal memperbarui foto: $newUrlOrError", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    )

    if (isLoadingProfile && uid != null) { // Hanya tampilkan loading jika UID ada dan sedang loading
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uid == null && !isLoadingProfile) { // Jika UID null setelah loading selesai (atau tidak pernah mulai)
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Anda belum login.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navController.navigate("login") {popUpTo(0)} } ) {
                    Text("Login Sekarang")
                }
            }
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background), // Gunakan warna tema
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 16.dp) // Padding bawah untuk LazyColumn
        ) {
            item {
                Text(
                    text = "Profil Saya", // Judul lebih deskriptif
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp), // Padding lebih
                    style = MaterialTheme.typography.headlineSmall, // Style yang lebih sesuai
                    fontWeight = FontWeight.SemiBold // Sedikit tebal
                )
            }

            item {
                PhotoProfile(
                    photoUrl = photoUrl,
                    username = username, // Untuk inisial jika foto tidak ada
                    onPhotoClick = {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Tombol kecil di bawah foto untuk ganti foto, alternatif selain klik foto
                TextButton(onClick = {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Ganti Foto")
                }
            }

            item {
                Text(
                    text = if (username.isNotEmpty()) username else "Nama Belum Diatur",
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.titleLarge // Nama lebih besar
                )
            }

            item {
                DataProfile(
                    username = username,
                    nickname = nickname,
                    phoneNumber = phoneNumber,
                    email = email,
                    address = address,
                    navController = navController,
                    onSave = { fieldKey, newValue ->
                        if (uid != null) {
                            val updates: Map<String, Any> = mapOf(fieldKey to newValue)
                            val fieldMessage = fieldMessageMap[fieldKey] ?: fieldKey
                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                .updateChildren(updates).addOnSuccessListener {
                                    Toast.makeText(context, "$fieldMessage berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Gagal memperbarui $fieldMessage.", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun DataProfile(
    username: String,
    nickname: String,
    phoneNumber: String,
    email: String,
    address: String,
    navController: NavController,
    onSave: (String, String) -> Unit,


) {
    val context = LocalContext.current
    // Pemetaan label ke variabel database
    val fieldMap = mapOf(
        "Nama" to "name",
        "Nama Panggilan" to "nickname",
        "Nomor Telepon" to "phoneNumber",
        "Email" to "email",
        "Alamat" to "address"
    )

    val dataFields = listOf(
        "Nama" to username,"Nama Panggilan" to nickname, "Nomor Telepon" to phoneNumber, "Email" to email, "Alamat" to address
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        dataFields.forEach { (label, value) ->
            FieldDataProfile(label = label, value = value, onSave = { newValue ->
                val fieldKey = fieldMap[label] // Dapatkan kunci dari peta
                if (fieldKey != null) {
                    onSave(fieldKey, newValue) // Gunakan kunci yang sesuai
                } else {
                    Toast.makeText(
                        context, "Invalid field: $label", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        Text(text = "Ganti Kata Sandi",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight(500),
            color = Color.Blue,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { /*TODO*/ })
        Text(text = "Keluar",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight(500),
            color = Color.Red,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { navController.navigate("Login") })
    }
}

@Composable
fun FieldDataProfile(
    label: String, value: String, onSave: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }
    var isEditable: Boolean = true


    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isEditing) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        if (label == "Nomor Telepon" && newValue.all { it.isDigit() }) {
                            textFieldValue = newValue
                        } else if (label != "Nomor Telepon") {
                            textFieldValue = newValue
                        }
                    },
                    singleLine = true, modifier = Modifier.weight(1f)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                decorationBox = { innerTextField -> innerTextField() }
                )
                Row {
                    IconButton(onClick = {
                        onSave(textFieldValue)
                        isEditing = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Simpan",
                            tint = Color.Blue
                        )
                    }
                    IconButton(onClick = {
                        textFieldValue = value
                        isEditing = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Batal",
                            tint = Color.Red
                        )
                    }
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f)
                )
                if (label != "Email") {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 16.dp), thickness = 0.25.dp, color = Color.Gray
        )
    }
}

@Composable
fun PhotoProfile() {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.LightGray), // Warna default polos
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Foto",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )
    }
}