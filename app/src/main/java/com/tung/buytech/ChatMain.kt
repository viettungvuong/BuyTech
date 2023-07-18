package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.tung.buytech.AppController.Companion.db
import com.tung.buytech.AppController.Companion.messageFromUsers

class ChatMain : AppCompatActivity() {
    companion object{
        @JvmStatic
        //lấy tin nhắn gần nhất
        fun getMostRecentMessage(messages: CollectionReference, people: AppController.People, callback: (String, String) -> Unit){
            messages.orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
                .get() //lấy tin nhắn mới nhất
                .addOnSuccessListener {
                        querySnapshot->
                    if (!querySnapshot.isEmpty){
                        val lastDocument = querySnapshot.documents[0] //document chứa tin nhắn gần nhất
                        callback(lastDocument.getString("content").toString(),lastDocument.getString("timestamp").toString()) //gọi callback
                    }
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        messageFromUsers.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    //nếu có collection
                    getAllPeopleMessaged(messageFromUsers
                    )
                } else {
                    //nếu không có collection

                }
            } else {
                Log.d("Lỗi","Lỗi")
            }
        }

    }

    //lấy danh sách tất cả người đã nhắn
    fun getAllPeopleMessaged(messages: CollectionReference){
        messages.get().addOnSuccessListener {
            people ->
            for (person in people){
                //lấy từng id ra
            }
        }
    }


}