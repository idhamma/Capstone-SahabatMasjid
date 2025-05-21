package com.sepertigamalamdev.sahabatmasjid.management

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.auth.User
import com.sepertigamalamdev.sahabatmasjid.management.User as AppUser



class UserViewModel : ViewModel() {

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

                listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        username = snapshot.child("name").getValue(String::class.java) ?: ""
                        nickname = snapshot.child("nickname").getValue(String::class.java) ?: ""
                        email = snapshot.child("email").getValue(String::class.java) ?: ""
                        address = snapshot.child("address").getValue(String::class.java) ?: ""
                        phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java) ?: ""
                        isLoading = false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        isLoading = false
                        // Handle error jika perlu
                    }
                }

                userRef?.addValueEventListener(listener!!)
            }
        }

        override fun onCleared() {
            super.onCleared()
            listener?.let { userRef?.removeEventListener(it) }
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

