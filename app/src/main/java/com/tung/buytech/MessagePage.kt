package com.tung.buytech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.ktx.Firebase

class MessagePage : AppCompatActivity() {
    fun fetchMessages(product: Product){ //lấy tin nhắn
        val userId = Firebase.auth.currentUser?.uid

        //lấy những tin nhắn nhận
        AppController.db.collection(collectionProducts).document(product.productId)
            .collection("messages").where(
                Filter.or(
                    Filter.equalTo("receive", userId), Filter.equalTo("send", userId) //or query
                )
            ).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val content = document.get("content") as List<String> //nội dung tin nhắn
                    val sender = document.get("sender").toString()
                    val receive = document.get("receive").toString()
                    val productId = document.get("productId").toString()
                    //tìm product

                    val message=Message(content,sender, receive,)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }
}