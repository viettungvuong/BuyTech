package com.tung.buytech

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

class MessageAdapter(private val list: LinkedList<Message>): RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    inner class MessageHolder(val messageView: View): RecyclerView.ViewHolder(messageView){

        fun bind(message: Message){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_view,parent)
        return MessageHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val currentItem = list[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }

}