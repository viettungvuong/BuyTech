package com.tung.buytech

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.ChatFunctions.Companion.getMostRecentMessage
import com.tung.buytech.ChatFunctions.Companion.messageFromUsers
import java.util.*

class PeopleAdapter(private val list: LinkedList<AppController.People>): RecyclerView.Adapter<PeopleAdapter.PeopleHolder>()  {
    inner class PeopleHolder(val peopleView: View): RecyclerView.ViewHolder(peopleView){
        lateinit var chatName: TextView
        lateinit var lastMessage: TextView
        lateinit var lastSent: TextView
        init {
            chatName=peopleView.findViewById(R.id.name)
            lastMessage=peopleView.findViewById(R.id.lastMessage)
            lastSent=peopleView.findViewById(R.id.time)
        }
        fun bind(people: AppController.People){
            chatName.text=people.name
            getMostRecentMessage(messageFromUsers,people,{lastMessageContent,lastTimestamp->
                lastMessage.text=lastMessageContent
                lastSent.text=lastTimestamp
            })
            //callback để lấy nội dung tin nhắn cuối cùng
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.people_view,parent) //lấy view cho adapter
        return PeopleHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PeopleHolder, position: Int) {
        val currentItem = list[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }
}