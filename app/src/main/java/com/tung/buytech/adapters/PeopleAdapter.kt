package com.tung.buytech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tung.buytech.control.ChatFunctions.Companion.getMostRecentMessage
import com.tung.buytech.control.ChatFunctions.Companion.messageFromUsers
import com.tung.buytech.objects.PersonProduct
import com.tung.buytech.R
import java.util.*

class PersonAdapter(private val list: LinkedList<PersonProduct>): RecyclerView.Adapter<PersonAdapter.PersonHolder>()  {
    inner class PersonHolder(val PersonView: View): RecyclerView.ViewHolder(PersonView){
        lateinit var chatName: TextView
        lateinit var lastMessage: TextView
        lateinit var lastSent: TextView
        init {
            chatName=PersonView.findViewById(R.id.name)
            lastMessage=PersonView.findViewById(R.id.lastMessage)
            lastSent=PersonView.findViewById(R.id.time)
        }
        fun bind(PersonProduct: PersonProduct){
            chatName.text=PersonProduct.getPerson.name+" - "+PersonProduct.getProduct.name
            getMostRecentMessage(messageFromUsers,PersonProduct) { lastMessageContent, lastTimestamp ->
                lastMessage.text = lastMessageContent
                lastSent.text = lastTimestamp
            }
            //callback để lấy nội dung tin nhắn cuối cùng
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.person_view,parent) //lấy view cho adapter
        return PersonHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        val currentItem = list[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }
}