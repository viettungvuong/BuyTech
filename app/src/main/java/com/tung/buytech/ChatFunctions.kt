package com.tung.buytech

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.util.LinkedList

class ChatFunctions {
    companion object{
        @JvmField
        var messageFromUsers= AppController.db.collection("message"+ Firebase.auth.currentUser!!.uid)

        @JvmField
        var people = LinkedList<AppController.People>() //danh sách những người đã nhắn tin

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
}