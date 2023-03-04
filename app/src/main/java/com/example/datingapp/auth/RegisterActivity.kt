package com.example.datingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private var nickname = ""
    private var sex = ""
    private var location = ""
    private var age = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        // binding variables
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
            nickname = tietNickName.text.toString()
            sex = tietSex.text.toString()
            location = tietLocation.text.toString()
            age = tietAge.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userDataModel = UserDataModel(uid, nickname, sex, location, age)

                        FirebaseRefUtils.userInfoRef.child(uid).setValue(userDataModel)

                        val intentToMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(intentToMainActivity)

                    } else {

                    }
                }

        }

    }


}