package com.example.datingapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.databinding.ActivityMainBinding
import com.example.datingapp.setting.SettingActivity
import com.example.datingapp.slider.CardStackAdapter
import com.example.datingapp.utils.FirebaseAuthUtils
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var cardStackAdapter : CardStackAdapter
    lateinit var cardStackLayoutManager : CardStackLayoutManager

    private val usersDataList = mutableListOf<UserDataModel>()

    private var userCount = 0

    private val uid = FirebaseAuthUtils.getUid()
    private val TAG = "MainActivity"

    private lateinit var currentUserSex : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // binding variables
        val csvProfile = binding.csvProfile
        val imgLogout = binding.imgLogout

        imgLogout.setOnClickListener {
            val intentToSettingActivity = Intent(this, SettingActivity::class.java)
            startActivity(intentToSettingActivity)
        }

        cardStackLayoutManager = CardStackLayoutManager(baseContext, object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                if(direction == Direction.Right) {

                    userLikeOtherUser(uid, usersDataList[userCount].uid.toString())

                }else if(direction == Direction.Left) {

                }

                userCount++
                if(userCount == usersDataList.count()){
                    getUserDataList(currentUserSex)
                    Toast.makeText(this@MainActivity, "유저 데이터를 새롭게 받아옵니다.", Toast.LENGTH_SHORT).show()
                }
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

        getMyUserData()

    }

    private fun getMyUserData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                Log.d(TAG, data?.sex.toString())
                currentUserSex = data?.sex.toString()
                getUserDataList(data?.sex.toString())

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList(currentUserSex : String) {
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user?.sex.toString() == currentUserSex) {

                    }else {
                        usersDataList.add(user!!)
                    }
                }
                cardStackAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userInfoRef.addValueEventListener(postListener)

    }

    private fun userLikeOtherUser(myUid : String, otherUid : String) {
        FirebaseRefUtils.userLikeRef.child(myUid).child(otherUid).setValue("true")
        getOtherUserLikeList(otherUid)
    }

    private fun getOtherUserLikeList(otherUid : String) {
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children) {
                    val likeUserKey = dataModel.key.toString()

                    if(likeUserKey == uid) {
                        Toast.makeText(this@MainActivity, "매칭 완료", Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification()
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("매칭 완료")
            .setContentText("상대도 당신을 좋아해요!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this@MainActivity, "permission denied", Toast.LENGTH_SHORT).show()
                return
            }
            notify(123, builder.build())
        }


    }


}