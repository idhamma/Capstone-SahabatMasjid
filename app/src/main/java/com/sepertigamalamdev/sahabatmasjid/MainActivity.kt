package com.sepertigamalamdev.sahabatmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.sepertigamalamdev.sahabatmasjid.homepage.BorrowItemListScreen
import com.sepertigamalamdev.sahabatmasjid.homepage.BorrowScreen
import com.sepertigamalamdev.sahabatmasjid.homepage.HomepageScreen
import com.sepertigamalamdev.sahabatmasjid.barang.ItemListScreen
import com.sepertigamalamdev.sahabatmasjid.login.LoginScreen
import com.sepertigamalamdev.sahabatmasjid.peminjaman.PeminjamanScreen
import com.sepertigamalamdev.sahabatmasjid.profile.Profile
import com.sepertigamalamdev.sahabatmasjid.signup.SignUpScreen
import com.sepertigamalamdev.sahabatmasjid.barang.DetailBarangScreen
import com.sepertigamalamdev.sahabatmasjid.profile.ProfileUser
import com.sepertigamalamdev.sahabatmasjid.signup.SignUpScreen
import com.sepertigamalamdev.sahabatmasjid.succesScreen.SuccessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

//            simpanBarangDummy()
            val startDestination = "login"
            val navController = rememberNavController()

            SahabatMasjidTheme {
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") { LoginScreen(navController = navController) }
                    composable("signup") { SignUpScreen(navController = navController) }
                    composable("homepage") { HomepageScreen(navController = navController) }
                    composable("peminjaman") { PeminjamanScreen(navController = navController) }
                    composable("profile") { ProfileUser(navController = navController) }
                    composable("inventaris"){ItemListScreen(navController = navController)  }
                    composable("detailBarang/{id}") { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getString("id") ?: ""
                        DetailBarangScreen(navController = navController, itemId = itemId)
                    }
                    composable("pengajuanPeminjaman"){ BorrowScreen(navController = navController) }
                    composable("listBarangPinjam"){ ItemListScreen(navController = navController, true) }
                    composable("pengajuanPeminjamanLanjut") { BorrowScreen(navController = navController) }
                    composable("suksesPinjam"){ SuccessScreen(navController = navController) }
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