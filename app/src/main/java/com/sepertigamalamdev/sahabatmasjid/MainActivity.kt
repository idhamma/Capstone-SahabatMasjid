package com.sepertigamalamdev.sahabatmasjid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
//import com.sepertigamalamdev.sahabatmasjid.homepage.BorrowItemListScreen
import com.sepertigamalamdev.sahabatmasjid.peminjaman.BorrowScreen
import com.sepertigamalamdev.sahabatmasjid.homepage.HomepageScreen
import com.sepertigamalamdev.sahabatmasjid.management.barang.ItemListScreen
import com.sepertigamalamdev.sahabatmasjid.auth.LoginScreen
import com.sepertigamalamdev.sahabatmasjid.peminjaman.PeminjamanScreen
import com.sepertigamalamdev.sahabatmasjid.auth.SignUpScreen
import com.sepertigamalamdev.sahabatmasjid.management.barang.DetailBarangScreen
import com.sepertigamalamdev.sahabatmasjid.homepage.LandingScreen
import com.sepertigamalamdev.sahabatmasjid.management.barang.AddInventoryScreen
import com.sepertigamalamdev.sahabatmasjid.management.ConfirmDataScreen
import com.sepertigamalamdev.sahabatmasjid.management.DetailManageRoleScreen
import com.sepertigamalamdev.sahabatmasjid.management.ManageRequestScreen
import com.sepertigamalamdev.sahabatmasjid.management.OperatorBorrowDetailScreen
import com.sepertigamalamdev.sahabatmasjid.management.barang.BarangEditSection
import com.sepertigamalamdev.sahabatmasjid.peminjaman.DetailBorrowScreen
import com.sepertigamalamdev.sahabatmasjid.profile.ProfileUser
import com.sepertigamalamdev.sahabatmasjid.succesScreen.SuccessScreen
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

val supabase = createSupabaseClient(
    supabaseUrl = "https://dkmnqalzthzlkwjjfhmk.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRrbW5xYWx6dGh6bGt3ampmaG1rIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc5ODUxMjAsImV4cCI6MjA2MzU2MTEyMH0.Q9lxpX5mYsMxjhvRq2u43F_ZxGAcjWVygUHsNMn5GVc"
) {
    install(Postgrest)
    install(Storage)
}

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
                    startDestination = if (user != null) "landing" else "login"

//                    simpanBarangDummy()
                }

//                var context = LocalContext.current
//
//                tambahMasjid("Masjid Al-Ikhlas", "Jalan Merdeka No. 1") { success, errorMessage ->
//                    if (success) {
//                        Toast.makeText(context, "Berhasil menambahkan masjid", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(context, "Gagal: $errorMessage", Toast.LENGTH_SHORT).show()
//                    }
//                }



                // Tampilkan hanya jika startDestination sudah dicek
                if (startDestination != null) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                        composable("landing"){ LandingScreen(navController = navController) }
                        composable("login") { LoginScreen(navController = navController) }
                        composable("signup") { SignUpScreen(navController = navController) }
                        composable("homepage/{id}") { backStackEntry ->
                            val masjidId = backStackEntry.arguments?.getString("id") ?: ""
                            HomepageScreen(navController = navController, masjidId = masjidId)
                        }
                        composable("peminjaman") { PeminjamanScreen(navController = navController) }
                        composable("profile") { ProfileUser(navController = navController) }
                        composable(
                            route = "inventaris/{borrow}/{masjid}",
                            arguments = listOf(
                                navArgument("borrow") { type = NavType.BoolType },
                                navArgument("masjid") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val borrow = backStackEntry.arguments?.getBoolean("borrow") ?: false
                            val masjid = backStackEntry.arguments?.getString("masjid") ?: ""
                            ItemListScreen(
                                navController = navController,
                                borrow = borrow,
                                masjidid = masjid
                            )
                        }

                        composable("detailBarang/{masjidid}/{id}") { backStackEntry ->
                            val masjidId = backStackEntry.arguments?.getString("masjidid") ?: ""
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            DetailBarangScreen(navController = navController, masjidId = masjidId, itemId = itemId)
                        }

                        composable("editBarang/{id}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            BarangEditSection(navController = navController, itemId = itemId)
                        }

//                        composable("listBarangPinjam"){ ItemListScreen(navController = navController, true) }
                        composable("pengajuanPeminjaman/{id}") { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            BorrowScreen(navController = navController, itemId = itemId)
                        }
                        composable("detailPeminjaman/{id}") { backStackEntry ->
                            val borrowId = backStackEntry.arguments?.getString("id") ?: ""
                            DetailBorrowScreen(navController = navController, borrowId = borrowId)
                        }
                        composable("suksesPinjam") { SuccessScreen(navController = navController) }

                        composable("tambahBarang/{id}"){ backStackEntry ->
                            val masjidId = backStackEntry.arguments?.getString("id") ?: ""
                            AddInventoryScreen(navController = navController, masjidId = masjidId)
                        }

                        composable("applyJemaah/{id}") { backStackEntry ->
                            val masjidId = backStackEntry.arguments?.getString("id") ?: ""
                            ConfirmDataScreen(navController = navController, masjidid = masjidId)
                        }

                        composable("manageJemaah/{id}"){backStackEntry ->
                            val masjidId = backStackEntry.arguments?.getString("id") ?: ""
                            ManageRequestScreen(navController = navController,masjidId)
                        }


                        composable("manageDetailJemaah/{id}"){backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("id") ?: ""
                            DetailManageRoleScreen(navController = navController,itemId = itemId)
                        }

                        composable(
                            route = "operator_detail_peminjaman/{borrowId}", // Nama route bisa disesuaikan
                            arguments = listOf(
                                navArgument("borrowId") {
                                    type = NavType.StringType // Tipe argumen adalah String
                                    // nullable = false // Tidak perlu jika bagian dari path
                                }
                            )
                        ) { backStackEntry ->
                            val borrowId = backStackEntry.arguments?.getString("borrowId")
                            if (borrowId != null) {
                                OperatorBorrowDetailScreen(
                                    navController = navController,
                                    borrowId = borrowId
                                    // ViewModel akan dibuat secara default di dalam OperatorBorrowDetailScreen
                                )
                            } else {
                                // Handle kasus jika borrowId null, meskipun seharusnya tidak terjadi
                                // jika navigasi dilakukan dengan benar.
                                Text("Error: ID Peminjaman untuk Operator tidak valid.")
                            }
                        }

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