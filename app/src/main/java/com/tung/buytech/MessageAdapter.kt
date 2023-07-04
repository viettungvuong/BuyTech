package com.tung.buytech

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

class MessageAdapter(private val list: LinkedList<AppController.Message>): RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    inner class MessageHolder(val messageView: View): RecyclerView.ViewHolder(messageView){
        fun bind(message: AppController.Message){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val currentItem = list[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }

}