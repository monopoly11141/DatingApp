package com.example.datingapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.datingapp.auth.IntroActivity
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.databinding.ActivityMainBinding
import com.example.datingapp.slider.CardStackAdapter
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.google.firebase.ktx.Firebase as Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var cardStackAdapter : CardStackAdapter
    lateinit var cardStackLayoutManager : CardStackLayoutManager

    private val usersDataList = mutableListOf<UserDataModel>()

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

        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        csvProfile.layoutManager = cardStackLayoutManager
        csvProfile.adapter = cardStackAdapter

        getDataUserDataList()

    }

    private fun getDataUserDataList() {
        // Read from the database
        FirebaseRefUtils.userInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    usersDataList.add(user!!)

                }
                cardStackAdapter.notifyItemInserted(0)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}