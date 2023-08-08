package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.tung.buytech.AppController.Companion.bindProductPerson
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.ChatFunctions.Companion.messageFromUsers
import com.tung.buytech.ChatFunctions.Companion.PersonProducts

class ChatMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        val messagesRecyclerView=findViewById<RecyclerView>(R.id.messagesList)
        val messageAdapter=PersonAdapter(PersonProducts)

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