package com.example.datingapp.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel

class CardStackAdapter(val context : Context, val items : List<UserDataModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val profileName = itemView.findViewById<TextView>(R.id.tvProfileName)
        val profileAge = itemView.findViewById<TextView>(R.id.tvProfileAge)
        val profileCity = itemView.findViewById<TextView>(R.id.tvProfileCity)

        fun binding(data : UserDataModel) {

            profileName.text = data.nickname
            profileAge.text = data.age
            profileCity.text = data.location

        }
    }

}