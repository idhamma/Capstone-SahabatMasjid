package com.sepertigamalamdev.sahabatmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
//import com.sepertigamalamdev.sahabatmasjid.homepage.BorrowItemListScreen
import com.sepertigamalamdev.sahabatmasjid.peminjaman.BorrowScreen
import com.sepertigamalamdev.sahabatmasjid.homepage.HomepageScreen
import com.sepertigamalamdev.sahabatmasjid.barang.ItemListScreen
import com.sepertigamalamdev.sahabatmasjid.auth.LoginScreen
import com.sepertigamalamdev.sahabatmasjid.peminjaman.PeminjamanScreen
import com.sepertigamalamdev.sahabatmasjid.auth.SignUpScreen
import com.sepertigamalamdev.sahabatmasjid.barang.DetailBarangScreen
import com.sepertigamalamdev.sahabatmasjid.profile.ProfileUser
import com.sepertigamalamdev.sahabatmasjid.succesScreen.SuccessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SahabatMasjidTheme {
                val navController = rememberNavController()

                // State untuk menampung start destination
                var startDestination by remember { mutableStateOf<String?>(null) }

                // Cek apakah user sudah login atau belum
                LaunchedEffect(Unit) {
                    val user = FirebaseAuth.getInstance().currentUser
                    startDestination = if (user != null) "homepage" else "login"
                }

                // Tampilkan hanya jika startDestination sudah dicek
                if (startDestination != null) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                        composable("login") { LoginScreen(navController = navController) }
                        composable("signup") { SignUpScreen(navController = navController) }
                        composable("homepage") { HomepageScreen(navController = navController) }
                        composable("peminjaman") { PeminjamanScreen(navController = navController) }
                        composable("profile") { ProfileUser(navController = navController) }
                        composable("inventaris") { ItemListScreen(navController = navController) }
                        composable("detailBarang/{id}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            DetailBarangScreen(navController = navController, itemId = itemId)
                        }
                        composable("listBarangPinjam"){ ItemListScreen(navController = navController, true) }
                        composable("pengajuanPeminjaman/{id}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            BorrowScreen(navController = navController, itemId = itemId)
                        }
                        composable("suksesPinjam") { SuccessScreen(navController = navController) }
                    }
                } else {
                    // Tampilan loading sementara pengecekan status login
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        ,
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


@Composable
fun SahabatMasjidTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF4CAF50), // Warna primer (hijau)
        secondary = Color(0xFF2196F3), // Warna sekunder (biru)
        background = Color.White,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}