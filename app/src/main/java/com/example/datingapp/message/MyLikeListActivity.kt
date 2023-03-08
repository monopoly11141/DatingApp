package com.example.datingapp.message

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel
import com.example.datingapp.databinding.ActivityMyLikeListBinding
import com.example.datingapp.message.fcm.NotificationModel
import com.example.datingapp.message.fcm.PushNotification
import com.example.datingapp.message.fcm.RetrofitInstance
import com.example.datingapp.utils.FirebaseAuthUtils
import com.example.datingapp.utils.FirebaseRefUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLikeListActivity : AppCompatActivity() {

    private val TAG = "MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()
    private val likeUserUidList = mutableListOf<String>()
    private val likeUserList = mutableListOf<UserDataModel>()
    private lateinit var binding: ActivityMyLikeListBinding
    private lateinit var lvLikeUserAdapter: LVLikeUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_like_list)

        val lvUserList = binding.lvUserList

        lvLikeUserAdapter = LVLikeUserAdapter(this, likeUserList)
        lvUserList.adapter = lvLikeUserAdapter


        //내가 좋아하는 사람들 리스트
        getMyListList()

        lvUserList.setOnItemClickListener { parent, view, position, id ->
            checkMatching(likeUserList[position].uid.toString())
            //Log.d(TAG, likeUserList[position].uid.toString())

            val notificationModel = NotificationModel("a", "b")

            val pushModel = PushNotification(notificationModel, likeUserList[position].token.toString())

            testPush(pushModel)
        }

    }

    private fun getMyListList() {
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    likeUserUidList.add(dataModel.key.toString())
                }
                getUserDataList()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    private fun getUserDataList() {
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {

                    val user = dataModel.getValue(UserDataModel::class.java)

                    if (likeUserUidList.contains(user?.uid)) {
                        likeUserList.add(user!!)
                    }


                }
                lvLikeUserAdapter.notifyDataSetChanged()
                Log.d(TAG, "LikeUserList :$likeUserList")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userInfoRef.addValueEventListener(postListener)

    }

    private fun checkMatching(clickedUid: String) {
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) =
                if (dataSnapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeListActivity, "매칭이 되지 않았습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    for (dataModel in dataSnapshot.children) {
                        val likeUserKey = dataModel.key.toString()
                        if (likeUserKey == uid) {
                            Toast.makeText(
                                this@MyLikeListActivity,
                                "매칭이 되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            break
                        } else {
                        }

                    }

                }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException())
            }
        }
        FirebaseRefUtils.userLikeRef.child(clickedUid).addValueEventListener(postListener)
    }

    private fun testPush(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        RetrofitInstance.api.postNotification(notification)

    }


}