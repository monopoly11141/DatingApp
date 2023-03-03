package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val tietEmail = binding.tietEmail
        val tietPassword = binding.tietPassword
        val tietPasswordDoubleCheck = binding.tietPasswordDoubleCheck
        val tietNickName = binding.tietNickName
        val tietSex = binding.tietSex
        val tietLocation = binding.tietLocation
        val tietAge = binding.tietAge
        val btnRegister = binding.btnRegister

        btnRegister.setOnClickListener {
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser?.uid

                        val intentToMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(intentToMainActivity)

                    } else {

                    }
                }

        }

    }


}