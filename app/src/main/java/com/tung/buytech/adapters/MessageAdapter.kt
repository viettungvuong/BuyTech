package com.tung.buytech.adapters

import android.content.Context
import android.graphics.ColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.objects.Message
import com.tung.buytech.R
import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.formatter
import com.tung.buytech.objects.MessageContent
import java.time.LocalDateTime
import java.util.LinkedList

class MessageAdapter(private val context: Context, private val message: Message): RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    inner class MessageHolder(val messageView: View): RecyclerView.ViewHolder(messageView){

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(content: MessageContent){
            val userId = Firebase.auth.currentUser!!.uid
            (messageView as ConstraintLayout).setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.white))

            if (content.senderId == userId){ //nếu tin nhắn do người dùng hiện tại gửi thì đặt thành màu xanh
                messageView.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.blue1))
            }

            messageView.findViewById<TextView>(R.id.content).text=content.text
            messageView.findViewById<TextView>(R.id.time).text= content.time.format(formatter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_view,parent)
        return MessageHolder(itemView)
    }

    override fun getItemCount(): Int {
        return message.getContent.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val currentItem = message.getContent[position] //lấy vật ở vị trí thứ position trong list
        holder.bind(currentItem)
    }

}