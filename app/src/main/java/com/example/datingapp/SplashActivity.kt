package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.datingapp.auth.IntroActivity
import com.example.datingapp.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUserUid = FirebaseAuthUtils.getUid()

        //user not logged in
        if(currentUserUid == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intentToIntroActivity = Intent(this, IntroActivity::class.java)
                startActivity(intentToIntroActivity)
                finish()
            }, 1500)
        }else { //user logged in
            Handler(Looper.getMainLooper()).postDelayed({
                val intentToMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentToMainActivity)
                finish()
            }, 1500)
        }


    }
}