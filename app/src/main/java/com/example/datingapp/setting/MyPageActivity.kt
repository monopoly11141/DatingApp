package com.example.datingapp.setting

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.utils.FirebaseAuthUtils
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text

class MyPageActivity : AppCompatActivity() {

    private var uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        getMyData()
    }

    private fun getMyData() {

        val imgMyImg = findViewById<ImageView>(R.id.imgMyImg)
        val tvMyUid = findViewById<TextView>(R.id.tvMyUid)
        val tvMyNickname = findViewById<TextView>(R.id.tvMyNickname)
        val tvAge = findViewById<TextView>(R.id.tvMyAge)
        val tvMyLocation = findViewById<TextView>(R.id.tvMyLocation)
        val tvMySex = findViewById<TextView>(R.id.tvMySex)

        Log.d("uidvalue", uid)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                tvMyUid.text = data!!.uid
                tvMyNickname.text = data.nickname
                tvAge.text = data.age
                tvMyLocation.text = data.location
                tvMySex.text = data.sex

                val storageRef = Firebase.storage.reference.child("${data.uid}.png")
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->

                    if(task.isSuccessful) {
                        Glide.with(baseContext).load(task.result).into(imgMyImg)
                    }

                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}