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
                    val contents = document.get("content") as List<Map<String,Any>> //nội dung tin nhắn là array of map
                    val a= document.get("a").toString()
                    val b = document.get("b").toString()
                    val productId = document.get("productId").toString()

                    val messageContents = ArrayList<MessageContent>()

                    //đổi content
                    for (content in contents){
                        val text = content["text"].toString()
                        val sender = content["sender"].toString()
                        val recipient = content["recipient"].toString()
                        val time = content["time"].toString() //convert to datetime

                        messageContents.add(MessageContent(text,sender,recipient,time))
                    }

                    AppController.bindProductById(productId){
                        bindedProduct->
                        val message=Message(messageContents,a,b,bindedProduct)
                    }

                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }
}