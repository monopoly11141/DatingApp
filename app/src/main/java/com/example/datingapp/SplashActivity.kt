package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.datingapp.auth.IntroActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intentToIntroActivity = Intent(this, IntroActivity::class.java)
            startActivity(intentToIntroActivity)
            finish()
        }, 1500) // 3000 is the delayed time in milliseconds.
    }
}