package com.example.datingapp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRefUtils {

    companion object {
        val database = Firebase.database

        val userInfoRef = database.getReference("user info")
    }

}