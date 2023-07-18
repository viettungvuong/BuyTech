package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.messageFromUser

class ChatMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        messageFromUser.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    //nếu có collection
                    getAllPeopleMessaged(messageFromUser)
                } else {
                    //nếu không có collection

                }
            } else {
                Log.d("Lỗi","Lỗi")
            }
        }

    }

    //lấy danh sách tất cả người đã nhắn
    fun getAllPeopleMessaged(collectionReference: CollectionReference){
        collectionReference.get().addOnSuccessListener {
            people ->
            for (person in people){
                //lấy từng id ra
            }
        }
    }

    //lấy tin nhắn gần nhất
    fun getMostRecentMessage(people: AppController.People){

    }
}