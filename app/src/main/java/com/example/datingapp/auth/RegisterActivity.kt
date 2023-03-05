package com.example.datingapp.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

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
        val imgProfile = binding.imgProfile
        val tietEmail = binding.tietEmail
        val tietPassword = binding.tietPassword
        val tietPasswordDoubleCheck = binding.tietPasswordDoubleCheck
        val tietNickName = binding.tietNickName
        val tietSex = binding.tietSex
        val tietLocation = binding.tietLocation
        val tietAge = binding.tietAge
        val btnRegister = binding.btnRegister

        imgProfile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startForResult.launch(gallery)
        }

        btnRegister.setOnClickListener {
            val email = tietEmail.text.toString()
            val password = tietPassword.text.toString()
            nickname = tietNickName.text.toString()
            sex = tietSex.text.toString()
            location = tietLocation.text.toString()
            age = tietAge.text.toString()

            if(email.isEmpty()) {
                Toast.makeText(this, "이메일 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userDataModel = UserDataModel(uid, nickname, sex, location, age)

                        FirebaseRefUtils.userInfoRef.child(uid).setValue(userDataModel)

                        uploadImage(uid)

                        val intentToMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(intentToMainActivity)

                    } else {
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            binding.imgProfile.setImageURI(intent?.data)
        }
    }

    private fun uploadImage(uid : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference.child("${uid}.png")

        val imageView = binding.imgProfile
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }


}