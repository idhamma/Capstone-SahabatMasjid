//package com.sepertigamalamdev.sahabatmasjid.viewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.auth.User
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import com.sepertigamalamdev.sahabatmasjid.management.user
//
//
//class UserViewModel : ViewModel() {
//    private val db = FirebaseFirestore.getInstance()
//    private val auth = FirebaseAuth.getInstance()
//    private val _user = MutableLiveData<user?>()
//    val user: LiveData<user?> = _user
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    fun fetchUser() {
//        val uid = auth.currentUser?.uid ?: return
//        viewModelScope.launch {
//            try {
//                val snapshot = db.collection("users").document(uid).get().await()
//                if (snapshot.exists()) {
//                    val fetchedUser = snapshot.toObject(User::class.java)
//                    _user.postValue(fetchedUser)
//                    _error.postValue(null)
//                } else {
//                    _error.postValue("User not found")
//                }
//            } catch (e: Exception) {
//                _error.postValue("Failed to fetch user: ${e.message}")
//            }
//        }
//    }
//}