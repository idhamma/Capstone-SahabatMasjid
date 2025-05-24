package com.sepertigamalamdev.sahabatmasjid.management

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.sepertigamalamdev.sahabatmasjid.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject
import com.sepertigamalamdev.sahabatmasjid.management.User as AppUser

// Di UserViewModel.kt
import io.ktor.client.* // Contoh jika menggunakan Ktor
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.* // Untuk ContentNegotiation
import io.ktor.serialization.kotlinx.json.* // Untuk serialisasi JSON dengan Kotlinx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream

//import io.github.jan.supabase.gotrue.Session

@Serializable
data class SupabaseCustomTokenResponse(
    val supabaseToken: String?,
    val error: String? = null
)

class UserViewModel : ViewModel() {

    @SuppressLint("RestrictedApi")
    class UserProfileViewModel : ViewModel() {
        var username by mutableStateOf("")
            internal set
        var nickname by mutableStateOf("")
            private set
        var phoneNumber by mutableStateOf("")
            internal set
        var email by mutableStateOf("")
            internal set
        var address by mutableStateOf("")
            internal set
        var photoUrl by mutableStateOf("") // <-- TAMBAHKAN INI
            internal set
        var isLoading by mutableStateOf(true)
            private set

        private val auth = FirebaseAuth.getInstance()
        private val uid = auth.currentUser?.uid
        private var userRef: DatabaseReference? = null
        private var listener: ValueEventListener? = null

        init {
            uid?.let { id ->
                val database = FirebaseDatabase.getInstance()
                userRef = database.getReference("users").child(id)
                Log.d("UserProfileVM", "Initializing listener for UID: $id on path: ${userRef?.path}")

                listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("UserProfileVM", "Firebase onDataChange triggered. Snapshot exists: ${snapshot.exists()}")
                        if (snapshot.exists()) {
                            username = snapshot.child("name").getValue(String::class.java) ?: ""
                            nickname = snapshot.child("nickname").getValue(String::class.java) ?: ""
                            email = snapshot.child("email").getValue(String::class.java) ?: ""
                            address = snapshot.child("address").getValue(String::class.java) ?: ""
                            phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java) ?: ""

                            // --- MODIFIKASI DIMULAI DI SINI ---
                            val newPhotoUrl = snapshot.child("photoUrl").getValue(String::class.java) ?: ""
                            if (photoUrl != newPhotoUrl) { // Hanya log jika ada perubahan aktual
                                Log.d("UserProfileVM", "Firebase ValueEventListener: photoUrl DIUPDATE di ViewModel menjadi: '$newPhotoUrl' (sebelumnya: '$photoUrl')")
                            } else if (photoUrl == newPhotoUrl && photoUrl.isNotBlank()){
                                Log.d("UserProfileVM", "Firebase ValueEventListener: photoUrl SAMA (tidak berubah dari '$newPhotoUrl'), tidak ada update state.")
                            } else {
                                Log.d("UserProfileVM", "Firebase ValueEventListener: newPhotoUrl adalah '$newPhotoUrl', photoUrl saat ini '$photoUrl'.")
                            }
                            photoUrl = newPhotoUrl // Update state photoUrl di ViewModel
                            // --- MODIFIKASI SELESAI DI SINI ---

                        } else {
                            Log.w("UserProfileVM", "Snapshot tidak ada untuk UID: $id")
                            // Set ke default jika snapshot tidak ada
                            username = ""
                            nickname = ""
                            email = ""
                            address = ""
                            phoneNumber = ""
                            photoUrl = ""
                        }
                        isLoading = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("UserProfileVM", "Firebase listener cancelled: ${error.message}")
                        isLoading = false
                        // Handle error jika perlu
                    }
                }
                userRef?.addValueEventListener(listener!!)
            } ?: run {
                isLoading = false // Jika UID null, berhenti loading
                Log.w("UserProfileVM", "UID is null, cannot fetch profile.")
            }
        }

        override fun onCleared() {
            super.onCleared()
            listener?.let { userRef?.removeEventListener(it) }
            Log.d("UserProfileVM", "ViewModel cleared, listener removed.")
        }
    }


    fun getUserRole(uid: String?,masjidid: String?, onResult: (String?) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("role")
        val uid_masjidid_filter = "$uid-$masjidid"
        dbRef.orderByChild("uid_masjidid").equalTo(uid_masjidid_filter)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var result: String? = null
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val role = child.getValue(Role::class.java)
                            if (role != null) {
                                result = role.status
                                break
                            }
                        }
                    }
                    onResult(result)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun getUserByUid(uid: String, onResult: (AppUser?) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(AppUser::class.java)
                onResult(user)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    // --- Gunakan List<Masjid> di sini ---
    private val _managedMasjidsInfo = MutableStateFlow<List<Masjid>>(emptyList())
    val managedMasjidsInfo: StateFlow<List<Masjid>> = _managedMasjidsInfo.asStateFlow()

    fun loadUserManagementRoles(userId: String?) {
        if (userId.isNullOrBlank()) {
            _managedMasjidsInfo.value = emptyList()
            Log.w("UserViewModel", "loadUserManagementRoles: userId is null or blank.")
            return
        }
        val rolesRef = FirebaseDatabase.getInstance().getReference("role") // Sesuaikan nama node
        val masjidsRef = FirebaseDatabase.getInstance().getReference("masjid") // Sesuaikan nama node

        rolesRef.orderByChild("uid").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(roleSnapshots: DataSnapshot) {

                    if (!roleSnapshots.exists() || roleSnapshots.childrenCount == 0L) { // Added childrenCount check
                        _managedMasjidsInfo.value = emptyList()

                        return
                    }

                    val approvedMasjidIds = mutableSetOf<String>()
                    roleSnapshots.children.forEach { roleSnapshot -> // This loop should now execute if childrenCount > 0
                        val role = roleSnapshot.getValue(Role::class.java)

                        Log.d("UserViewModel_RoleDetail", "--- Processing Role Snapshot Key: ${roleSnapshot.key} ---")
                        if (role == null) {
                            Log.d("UserViewModel_RoleDetail", "Role object deserialized as NULL.")
                        } else {
                            Log.d("UserViewModel_RoleDetail", "Role Object: id='${role.id}', uid='${role.uid}', masjidid='${role.masjidid}', uid_masjidid='${role.uid_masjidid}', status='${role.status}'")
                            val isMasjidIdNotBlank = role.masjidid.isNotBlank()
                            val isStatusOperator = role.status.equals("operator", ignoreCase = true)
                            Log.d("UserViewModel_RoleDetail", "Kondisi: role.masjidid.isNotBlank() -> $isMasjidIdNotBlank")
                            Log.d("UserViewModel_RoleDetail", "Kondisi: role.status.equals(\"operator\", ignoreCase = true) -> $isStatusOperator")

                            if (isMasjidIdNotBlank && isStatusOperator) {
                                approvedMasjidIds.add(role.masjidid)
                                Log.d("UserViewModel_RoleDetail", "SUCCESS: Added masjidId '${role.masjidid}' for role with status '${role.status}'.")
                            } else {
                                Log.d("UserViewModel_RoleDetail", "SKIPPED: Role for masjidId '${role.masjidid}' with status '${role.status}'. Reason: masjidIdNotBlank=${role.masjidid.isNotBlank()}, statusMatch=${isStatusOperator}")
                            }
                        }
                    }
                    Log.d("UserViewModel_RoleDetail", "Final count of approvedMasjidIds: ${approvedMasjidIds.size}")


                    if (approvedMasjidIds.isEmpty()) {
                        _managedMasjidsInfo.value = emptyList()
                        Log.d("UserViewModel", "No approved management roles found for UID: $userId based on status check.")
                        return
                    }

                    val masjidDetailTasks = approvedMasjidIds.map { masjidId ->
                        masjidsRef.child(masjidId).get()
                    }

                    Tasks.whenAllSuccess<DataSnapshot>(masjidDetailTasks)
                        .addOnSuccessListener { masjidDataSnapshotsList ->
                            val newManagedMasjidsList = mutableListOf<Masjid>() // <-- Tipe diubah ke Masjid
                            masjidDataSnapshotsList.forEach { masjidDataSnapshot ->
                                val masjid = masjidDataSnapshot.getValue(Masjid::class.java) // <-- Langsung deserialize ke Masjid
                                if (masjid != null && masjid.id.isNotBlank() && masjid.name.isNotBlank()) {
                                    newManagedMasjidsList.add(masjid) // <-- Tambahkan objek Masjid
                                } else {
                                    Log.w("UserViewModel", "Masjid data incomplete or null for snapshot: ${masjidDataSnapshot.key}")
                                }
                            }
                            // Pastikan id unik jika ada kemungkinan duplikasi dari sumber data peran
                            _managedMasjidsInfo.value = newManagedMasjidsList.distinctBy { it.id }
                            Log.d("UserViewModel", "Managed masjids loaded: ${newManagedMasjidsList.size}")
                        }
                        .addOnFailureListener { exception ->
                            _managedMasjidsInfo.value = emptyList()
                            Log.e("UserViewModel", "Error fetching masjid details for roles: ${exception.message}")
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    _managedMasjidsInfo.value = emptyList()
                    Log.e("UserViewModel", "Firebase roles query cancelled: ${error.message}")
                }
            })
    }

    //profile photo


    // Anda mungkin juga perlu fungsi untuk hanya memperbarui URL foto di Firebase jika URL sudah ada
    fun updateUserPhotoUrlInFirebase(userId: String, photoUrl: String, onResult: (Boolean) -> Unit) {
        if (userId.isBlank()) {
            onResult(false)
            return
        }
        val userPhotoUrlRef = FirebaseDatabase.getInstance().getReference("users")
            .child(userId)
            .child("photoUrl")
        userPhotoUrlRef.setValue(photoUrl)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


    private val httpClient = HttpClient { // Anda mungkin perlu HttpClient(Android)
        install(ContentNegotiation) {
            json() // Menggunakan Kotlinx.serialization.json
        }
    }

    // Fungsi untuk mendapatkan JWT kustom dari backend Anda
    private suspend fun getSupabaseCustomToken(firebaseIdToken: String): String? {
        // Ganti dengan URL Cloud Function/Backend Anda yang sebenarnya
        val tokenExchangeUrl = "URL_CLOUD_FUNCTION_ANDA_UNTUK_TUKAR_TOKEN"
        try {
            Log.d("UserViewModel", "Requesting Supabase custom token from: $tokenExchangeUrl")
            val response: SupabaseCustomTokenResponse = httpClient.post(tokenExchangeUrl) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("firebaseToken" to firebaseIdToken)) // Kirim Firebase ID Token
            }.body() // Deserialize respons

            if (response.supabaseToken != null) {
                Log.d("UserViewModel", "Supabase custom token received.")
                return response.supabaseToken
            } else {
                Log.e("UserViewModel", "Failed to get Supabase custom token: ${response.error ?: "Unknown error from backend"}")
                return null
            }
        } catch (e: Exception) {
            Log.e("UserViewModel", "Exception during custom token exchange: ${e.message}", e)
            return null
        }
    }

    // Di UserViewModel.kt, dalam fungsi uploadProfilePhotoDirectly

    fun uploadProfilePhotoDirectly(
        userId: String,
        imageUri: Uri,
        context: Context,
        bucketName: String = "profiles",
        onResult: (success: Boolean, newUrlOrError: String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Dapatkan InputStream dari Uri
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                if (inputStream == null) {
                    onResult(false, "Gagal membuka file gambar sumber.")
                    return@launch
                }

                // --- MULAI PROSES KOMPRESI ---
                val compressedImageBytes: ByteArray = withContext(Dispatchers.IO) { // Jalankan di background thread
                    // 1. Decode InputStream menjadi Bitmap
                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close() // Tutup stream setelah selesai

                    if (originalBitmap == null) {
                        // Tidak bisa return langsung dari withContext ke onResult,
                        // jadi lempar exception atau kembalikan null/flag error
                        throw Exception("Gagal men-decode gambar menjadi Bitmap.")
                    }

                    // 2. (Opsional) Ubah Ukuran (Scaling) Bitmap
                    // Tentukan ukuran target, misalnya maksimal lebar/tinggi 1080px
                    val maxWidth = 1080
                    val maxHeight = 1080
                    val scaledBitmap = if (originalBitmap.width > maxWidth || originalBitmap.height > maxHeight) {
                        val ratio: Float = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                        val finalWidth: Int
                        val finalHeight: Int
                        if (ratio > 1) { // Landscape
                            finalWidth = maxWidth
                            finalHeight = (maxWidth / ratio).toInt()
                        } else { // Portrait atau Square
                            finalHeight = maxHeight
                            finalWidth = (maxHeight * ratio).toInt()
                        }
                        Log.d("ImageCompress", "Scaling bitmap from ${originalBitmap.width}x${originalBitmap.height} to ${finalWidth}x${finalHeight}")
                        Bitmap.createScaledBitmap(originalBitmap, finalWidth, finalHeight, true)
                    } else {
                        originalBitmap // Gunakan original jika sudah cukup kecil
                    }

                    // 3. Kompres Bitmap ke ByteArrayOutputStream
                    val outputStream = ByteArrayOutputStream()
                    val quality = 80 // Kualitas JPEG (0-100), 80 biasanya kompromi yang baik

                    // Pilih format kompresi: JPEG atau WEBP
                    // Untuk WEBP (jika API level mendukung dan Anda inginkan):
                    // scaledBitmap.compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
                    // Log.d("ImageCompress", "Compressing to WEBP with quality $quality")

                    // Untuk JPEG:
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    Log.d("ImageCompress", "Compressing to JPEG with quality $quality. Original size approx (pre-scale): ${originalBitmap.byteCount}, Scaled size approx: ${scaledBitmap.byteCount}")

                    val byteArray = outputStream.toByteArray()
                    Log.d("ImageCompress", "Final compressed byte array size: ${byteArray.size / 1024} KB")
                    byteArray // Kembalikan byte array hasil kompresi
                }
                // --- SELESAI PROSES KOMPRESI ---

                if (compressedImageBytes.isEmpty()) { // Seharusnya tidak terjadi jika kompresi berhasil
                    onResult(false, "Gagal mengompres gambar (hasil kosong).")
                    return@launch
                }

                // Path tetap penting untuk organisasi
                val fileExtension = context.contentResolver.getType(imageUri)?.split('/')?.lastOrNull() ?: "jpg"
                // Nama file bisa tetap sama atau dibuat unik, karena kontennya yang penting
                val filePath = "user_profiles/$userId/profile.$fileExtension" // Atau gunakan timestamp untuk path unik
                Log.d("UserViewModel", "Directly uploading COMPRESSED photo to Supabase Storage: $filePath in bucket $bucketName")

                val uploadResponsePath = supabase.storage[bucketName].upload(
                    path = filePath,
                    data = compressedImageBytes, // Gunakan byte array yang sudah dikompres
                    options = { upsert = true }
                )
                Log.d("UserViewModel", "Unggah ke Supabase berhasil (direct). Path: $uploadResponsePath")

                val basePublicUrl = supabase.storage[bucketName].publicUrl(filePath)
                // Tambahkan cache buster untuk memastikan UI mengambil versi terbaru jika URL/path sama
                val cacheBustedUrl = "${basePublicUrl}?timestamp=${System.currentTimeMillis()}"
                Log.d("UserViewModel", "URL Publik Supabase (dengan cache buster): $cacheBustedUrl")

                val userPhotoUrlRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId)
                    .child("photoUrl")
                userPhotoUrlRef.setValue(cacheBustedUrl).await()
                Log.d("UserViewModel", "URL Foto (dengan cache buster) berhasil disimpan ke Firebase untuk user $userId.")
                onResult(true, cacheBustedUrl)

            } catch (e: Exception) {
                Log.e("UserViewModel", "Error saat kompresi atau direct upload/update foto profil: ${e.message}", e)
                onResult(false, "Gagal memproses gambar: ${e.message}")
            }
        }
    }

}

class MasjidViewModel : ViewModel() {
    private val _masjid = mutableStateOf(Masjid())
    val masjid: State<Masjid> = _masjid

    private val database = FirebaseDatabase.getInstance()

    fun fetchMasjidData(masjidId: String) {
        val ref = database.getReference("masjid").child(masjidId)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedMasjid = snapshot.getValue(Masjid::class.java)
                loadedMasjid?.let {
                    _masjid.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // handle error (misal: log atau show toast)
            }
        })
    }

    fun updateNama(nama: String) {
        _masjid.value = _masjid.value.copy(name = nama)
    }

    fun updateAlamat(alamat: String) {
        _masjid.value = _masjid.value.copy(alamat = alamat)
    }

    fun getRequestsByMasjidId(masjidId: String, onResult: (List<RoleRequest>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("roleRequests")

        ref.orderByChild("masjidid").equalTo(masjidId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val requests = mutableListOf<RoleRequest>()
                    for (child in snapshot.children) {
                        val request = child.getValue(RoleRequest::class.java)
                        request?.let { requests.add(it) }
                    }
                    onResult(requests)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching role requests", error.toException())
                    onResult(emptyList())
                }
            })
    }
}

class PeminjamanViewModel : ViewModel() {
    private val _peminjamanBarangList = MutableStateFlow<List<Pair<Peminjaman, Barang>>>(emptyList())
    val peminjamanBarangList: StateFlow<List<Pair<Peminjaman, Barang>>> = _peminjamanBarangList

    fun loadPeminjamanBarang(uid: String) {
        val databasePeminjaman = FirebaseDatabase.getInstance().getReference("peminjaman")
        val databaseBarang = FirebaseDatabase.getInstance().getReference("barang")

        databasePeminjaman.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<Pair<Peminjaman, Barang>>()
                val peminjamanList = snapshot.children.mapNotNull { it.getValue(Peminjaman::class.java) }
                    .filter { it.uid == uid }

                if (peminjamanList.isEmpty()) {
                    _peminjamanBarangList.value = emptyList()
                    return
                }

                var counter = 0
                for (peminjaman in peminjamanList) {
                    databaseBarang.child(peminjaman.barangid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(barangSnapshot: DataSnapshot) {
                                val barang = barangSnapshot.getValue(Barang::class.java)
                                if (barang != null) {
                                    tempList.add(peminjaman to barang)
                                }
                                counter++
                                if (counter == peminjamanList.size) {
                                    _peminjamanBarangList.value = tempList
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                counter++
                                if (counter == peminjamanList.size) {
                                    _peminjamanBarangList.value = tempList
                                }
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private val _peminjaman = mutableStateOf<Peminjaman?>(null)
    val peminjaman: State<Peminjaman?> = _peminjaman

    private val databaseRef = FirebaseDatabase.getInstance().getReference("peminjaman")

    fun getPeminjamanById(borrowId: String) {
        databaseRef.child(borrowId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _peminjaman.value = snapshot.getValue(Peminjaman::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PeminjamanViewModel", "Failed to get peminjaman: ${error.message}")
            }
        })
    }

    fun approvePeminjaman(peminjamanId: String, onResult: (Boolean) -> Unit) {
        Log.d("PeminjamanViewModel", "Attempting to approve peminjamanId: $peminjamanId")
        val peminjamanRef = FirebaseDatabase.getInstance().getReference("peminjaman").child(peminjamanId)
        peminjamanRef.child("status").setValue("disetujui")
            .addOnSuccessListener {
                Log.d("PeminjamanViewModel", "Peminjaman $peminjamanId approved successfully.")
                // Untuk memuat ulang data di screen setelah update:
                // Anda bisa memanggil getPeminjamanById(peminjamanId) lagi di sini agar _peminjaman.value terupdate,
                // atau Composable akan re-fetch berdasarkan perubahan `isSubmittingAction`.
                // Atau, jika Anda ingin update _peminjaman.value secara manual:
                // val currentPeminjaman = _peminjaman.value
                // if (currentPeminjaman != null && currentPeminjaman.id == peminjamanId) {
                //     _peminjaman.value = currentPeminjaman.copy(status = "disetujui")
                // }
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("PeminjamanViewModel", "Failed to approve peminjaman $peminjamanId: ${e.message}")
                onResult(false)
            }
    }

    fun rejectPeminjaman(
        peminjamanId: String,
        barangId: String,
        jumlahDikembalikan: Int,
        onResult: (Boolean) -> Unit
    ) {
        Log.d("PeminjamanViewModel", "Attempting to reject peminjamanId: $peminjamanId, barangId: $barangId, jumlah: $jumlahDikembalikan")
        if (peminjamanId.isBlank()) {
            Log.e("PeminjamanViewModel", "Peminjaman ID is blank for rejection.")
            onResult(false)
            return
        }

        val database = FirebaseDatabase.getInstance()
        val peminjamanRef = database.getReference("peminjaman").child(peminjamanId)

        peminjamanRef.child("status").setValue("ditolak")
            .addOnSuccessListener {
                Log.d("PeminjamanViewModel", "Peminjaman $peminjamanId status set to ditolak.")
                if (barangId.isNotBlank() && jumlahDikembalikan > 0) {
                    val barangRef = database.getReference("barang").child(barangId)
                    barangRef.child("stock").get()
                        .addOnSuccessListener { stockSnapshot ->
                            val currentStock = stockSnapshot.getValue(Int::class.java) ?: 0
                            val newStock = currentStock + jumlahDikembalikan
                            barangRef.child("stock").setValue(newStock)
                                .addOnSuccessListener {
                                    Log.d("PeminjamanViewModel", "Stock for barangId $barangId updated successfully.")
                                    onResult(true)
                                }
                                .addOnFailureListener { stockError ->
                                    Log.e("PeminjamanViewModel", "Failed to update stock for barangId $barangId: ${stockError.message}")
                                    onResult(false) // Status diubah, tapi stok gagal dikembalikan
                                }
                        }
                        .addOnFailureListener { getStockError ->
                            Log.e("PeminjamanViewModel", "Failed to get current stock for barangId $barangId: ${getStockError.message}")
                            onResult(false)
                        }
                } else {
                    // Tidak ada barangId atau jumlahDikembalikan <= 0, jadi hanya update status
                    Log.d("PeminjamanViewModel", "No stock update needed for barangId '$barangId' or jumlah $jumlahDikembalikan.")
                    onResult(true) // Status berhasil diubah
                }
            }
            .addOnFailureListener { statusError ->
                Log.e("PeminjamanViewModel", "Failed to reject peminjaman $peminjamanId: ${statusError.message}")
                onResult(false)
            }
    }

    // ... (StateFlow _peminjamanBarangList dan fungsi loadPeminjamanBarang yang sudah ada)
// ... (State _peminjaman dan fungsi getPeminjamanById yang sudah ada)
// ... (Fungsi approvePeminjaman dan rejectPeminjaman yang sudah Anda buat)

    private val _peminjamanUntukPengelolaan = MutableStateFlow<List<Pair<Peminjaman, Barang>>>(emptyList())
    val peminjamanUntukPengelolaan: StateFlow<List<Pair<Peminjaman, Barang>>> = _peminjamanUntukPengelolaan.asStateFlow()

    fun loadPeminjamanUntukPengelolaan(masjidIdTarget: String, statusFilter: String = "diajukan") {
        if (masjidIdTarget.isBlank()) {
            _peminjamanUntukPengelolaan.value = emptyList()
            Log.w("PeminjamanVM", "loadPeminjamanUntukPengelolaan: masjidIdTarget is blank.")
            return
        }
        Log.d("PeminjamanVM", "Loading peminjaman for management: Masjid '$masjidIdTarget', Status '$statusFilter'")

        val peminjamanRef = FirebaseDatabase.getInstance().getReference("peminjaman")
        val barangRef = FirebaseDatabase.getInstance().getReference("barang")

        // 1. Ambil semua peminjaman dengan status yang diinginkan
        peminjamanRef.orderByChild("status").equalTo(statusFilter)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(peminjamanSnapshots: DataSnapshot) {
                    if (!peminjamanSnapshots.exists()) {
                        _peminjamanUntukPengelolaan.value = emptyList()
                        Log.d("PeminjamanVM", "No peminjaman found with status: $statusFilter")
                        return
                    }

                    val candidatePeminjamanList = mutableListOf<Peminjaman>()
                    peminjamanSnapshots.children.forEach { peminjamanSnapshot ->
                        val peminjaman = peminjamanSnapshot.getValue(Peminjaman::class.java)
                        if (peminjaman != null && peminjaman.barangid.isNotBlank()) {
                            candidatePeminjamanList.add(peminjaman)
                        }
                    }

                    if (candidatePeminjamanList.isEmpty()) {
                        _peminjamanUntukPengelolaan.value = emptyList()
                        Log.d("PeminjamanVM", "No peminjaman items with valid barangid to process for status $statusFilter.")
                        return
                    }

                    val finalPairedList = mutableListOf<Pair<Peminjaman, Barang>>()
                    val totalCandidates = candidatePeminjamanList.size
                    var processedCandidates = 0

                    if (totalCandidates == 0) { // Safeguard
                        _peminjamanUntukPengelolaan.value = emptyList()
                        return
                    }

                    candidatePeminjamanList.forEach { peminjaman ->
                        // 2. Untuk setiap peminjaman, ambil data barangnya
                        barangRef.child(peminjaman.barangid).get()
                            .addOnCompleteListener { barangTask ->
                                processedCandidates++
                                if (barangTask.isSuccessful) {
                                    val barangSnapshot = barangTask.result
                                    val barang = barangSnapshot?.getValue(Barang::class.java)
                                    // 3. Filter berdasarkan masjidid dari barang
                                    if (barang != null && barang.masjidid == masjidIdTarget) {
                                        finalPairedList.add(Pair(peminjaman, barang))
                                    }
                                } else {
                                    Log.e("PeminjamanVM", "Failed to fetch barang ${peminjaman.barangid}: ${barangTask.exception?.message}")
                                }

                                // Setelah semua barang selesai (atau gagal) diambil
                                if (processedCandidates == totalCandidates) {
                                    _peminjamanUntukPengelolaan.value = finalPairedList
                                    Log.d("PeminjamanVM", "Finished processing. Found ${finalPairedList.size} items for masjid $masjidIdTarget for management.")
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("PeminjamanVM", "Firebase peminjaman query (by status) cancelled: ${error.message}")
                    _peminjamanUntukPengelolaan.value = emptyList()
                }
            })
    }

}


class BarangViewModel : ViewModel() {
    private val _barang = mutableStateOf<Barang?>(null)
    val barang: State<Barang?> = _barang

    private val databaseRef = FirebaseDatabase.getInstance().getReference("barang")

    fun getBarangById(id: String) {
        databaseRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _barang.value = snapshot.getValue(Barang::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BarangViewModel", "Failed to get barang: ${error.message}")
            }
        })
    }

        // --- FUNGSI BARU UNTUK UPLOAD GAMBAR BARANG ---

        fun uploadBarangImageAndUpdateDatabase(
            barangId: String,
            masjidId: String,
            imageUri: Uri,
            context: Context,
            bucketName: String = "product-images", // Nama bucket untuk gambar produk
            onResult: (success: Boolean, newImageUrlOrError: String?) -> Unit
        ) {
            if (barangId.isBlank() || masjidId.isBlank()) {
                onResult(false, "ID Barang atau ID Masjid tidak valid.")
                return
            }

            viewModelScope.launch {
                try {
                    val inputStream: InputStream? =
                        context.contentResolver.openInputStream(imageUri)
                    // --- LOGIKA KOMPRESI DAN SCALING GAMBAR (maxHeight = 1440px) ---
                    // (Sama seperti yang sudah kita bahas: decodeStream, createScaledBitmap, compress)
                    // Ganti bagian ini dengan implementasi kompresi lengkap Anda
                    val compressedImageBytes: ByteArray = withContext(Dispatchers.IO) {
                        val originalBitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                        if (originalBitmap == null) throw Exception("Gagal decode bitmap.")

                        val maxWidth = 1440
                        val maxHeight = 1440 // Sesuai permintaan Anda
                        val scaledBitmap =
                            if (originalBitmap.width > maxWidth || originalBitmap.height > maxHeight) {
                                val ratio: Float =
                                    originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
                                val finalWidth: Int
                                val finalHeight: Int
                                if (originalBitmap.width > originalBitmap.height) {
                                    finalWidth = maxWidth
                                    finalHeight = (maxWidth / ratio).toInt().coerceAtLeast(1)
                                } else {
                                    finalHeight = maxHeight
                                    finalWidth = (maxHeight * ratio).toInt().coerceAtLeast(1)
                                }
                                Bitmap.createScaledBitmap(
                                    originalBitmap,
                                    finalWidth,
                                    finalHeight,
                                    true
                                )
                            } else {
                                originalBitmap
                            }
                        val outputStream = ByteArrayOutputStream()
                        scaledBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            80,
                            outputStream
                        ) // Kualitas 80
                        outputStream.toByteArray()
                    }
                    // --- SELESAI KOMPRESI ---

                    if (compressedImageBytes.isEmpty()) {
                        onResult(false, "Gagal mengompres gambar barang.")
                        return@launch
                    }

                    val fileExtension =
                        context.contentResolver.getType(imageUri)?.split('/')?.lastOrNull() ?: "jpg"
                    // Path file statis per barang untuk menimpa gambar lama, atau gunakan timestamp untuk versi unik
                    val filePath = "product_images/$masjidId/$barangId/image.$fileExtension"

                    Log.d(
                        "BarangViewModel",
                        "Uploading COMPRESSED product image: $filePath in bucket $bucketName"
                    )
                    supabase.storage[bucketName].upload(
                        path = filePath, data = compressedImageBytes, options = { upsert = true }
                    )
                    Log.d("BarangViewModel", "Upload product image to Supabase successful.")

                    val basePublicUrl = supabase.storage[bucketName].publicUrl(filePath)
                    val cacheBustedUrl = "${basePublicUrl}?timestamp=${System.currentTimeMillis()}"
                    Log.d(
                        "BarangViewModel",
                        "Product image public URL (cache-busted): $cacheBustedUrl"
                    )

                    // Update field 'imageUrl' di Firebase
                    databaseRef.child(barangId).child("imageUrl").setValue(cacheBustedUrl).await()
                    Log.d(
                        "BarangViewModel",
                        "Product image URL saved to Firebase for barangId $barangId."
                    )
                    onResult(true, cacheBustedUrl)

                } catch (e: Exception) {
                    Log.e("BarangViewModel", "Error during product image upload: ${e.message}", e)
                    onResult(false, "Gagal memproses gambar barang: ${e.message}")
                }
            }
        }

    }


