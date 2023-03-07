package com.example.datingapp.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel

class LVLikeUserAdapter(val context : Context, val items : MutableList<UserDataModel>) : BaseAdapter(){
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.lv_like_user_item, parent, false)
        }

        val nickname : TextView = convertView!!.findViewById(R.id.tvNickname)
        nickname.text = items[position].nickname

        return convertView!!

    }
}