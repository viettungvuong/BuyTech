package com.tung.buytech.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.tung.buytech.R
import com.tung.buytech.control.AppController.Companion.bindProductPerson
import com.tung.buytech.control.ChatFunctions.Companion.messageFromUsers
import com.tung.buytech.control.ChatFunctions.Companion.PersonProducts
import com.tung.buytech.adapters.PersonAdapter

class ChatMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        val messagesRecyclerView=findViewById<RecyclerView>(R.id.messagesList)
        val messageAdapter= PersonAdapter(PersonProducts)

        messageFromUsers.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    //nếu có collection
                    getAllPersonMessaged(messageFromUsers,
                        {messagesRecyclerView.adapter=messageAdapter})
                } else {
                    //nếu không có collection

                }
            } else {
                //Log.d("Lỗi","Lỗi")
            }
        }

    }

    //lấy danh sách tất cả người đã nhắn
    fun getAllPersonMessaged(messages: CollectionReference, callback: ()->Unit){
        messages.get().addOnSuccessListener {
            documents ->
            for (document in documents){
                //lấy từng id của document ra (cũng là tên user id)
                bindProductPerson(document.id, {
                    bindedPersonProduct -> PersonProducts.add(bindedPersonProduct)
                })

            }
            callback()
        }
    }


}