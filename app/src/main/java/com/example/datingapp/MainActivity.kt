package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.datingapp.auth.IntroActivity
import com.example.datingapp.databinding.ActivityMainBinding
import com.example.datingapp.slider.CardStackAdapter
import com.google.firebase.auth.ktx.auth
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.google.firebase.ktx.Firebase as Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var cardStackAdapter : CardStackAdapter
    lateinit var cardStackLayoutManager : CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // binding variables
        val csvProfile = binding.csvProfile
        val imgLogout = binding.imgLogout

        imgLogout.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()

            val intentToIntroActivity = Intent(this, IntroActivity::class.java)
            startActivity(intentToIntroActivity)
        }

        cardStackLayoutManager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })

        val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")

        cardStackAdapter = CardStackAdapter(baseContext, testList)
        csvProfile.layoutManager = cardStackLayoutManager
        csvProfile.adapter = cardStackAdapter

    }
}