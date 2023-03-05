package com.example.datingapp.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object {

        private lateinit var auth: FirebaseAuth

        fun getUid() : String? {
            auth = FirebaseAuth.getInstance()
            if(auth.currentUser == null) {
                return null
            }else {
                return auth.currentUser?.uid.toString()
            }
        }
    }
}