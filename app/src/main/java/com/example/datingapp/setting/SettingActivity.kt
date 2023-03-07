package com.example.datingapp.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.datingapp.R
import com.example.datingapp.auth.IntroActivity
import com.example.datingapp.databinding.ActivitySettingBinding
import com.example.datingapp.message.MyLikeListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val btnMyPage = binding.btnMyPage
        val btnMyMatching = binding.btnMyMatching
        val btnLogout = binding.btnLogout

        btnMyPage.setOnClickListener {
            val intentToMyPageActivity = Intent(this, MyPageActivity::class.java)
            startActivity(intentToMyPageActivity)
        }

        btnMyMatching.setOnClickListener{
            val intentToMyLikeListActivity = Intent(this, MyLikeListActivity::class.java)
            startActivity(intentToMyLikeListActivity)
        }

        btnLogout.setOnClickListener{
            val auth = Firebase.auth
            auth.signOut()

            val intentToIntroActivity = Intent(this, IntroActivity::class.java)
            startActivity(intentToIntroActivity)
        }

    }
}