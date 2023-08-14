package com.tung.buytech.objects

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Filter
import com.google.firebase.ktx.Firebase
import com.tung.buytech.control.AppController
import com.tung.buytech.control.collectionProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class Message(private var content: List<MessageContent>, private var person1: String, private var person2: String, private var product: Product) {
    val getContent: List<MessageContent>
        get() = content

    val getPerson1: String
        get() = person1

    val getPerson2: String
        get() = person2

    val getProduct: Product
        get() = product
}

class MessageContent(val text: String, val senderId: String, val recipientId: String, val time: LocalDateTime)

class MessageController{
    private constructor()

    companion object{
        private var singleton: MessageController? =null

        @JvmStatic
        fun getSingleton(): MessageController{
            if (singleton==null){
                singleton=MessageController()
            }
            return singleton as MessageController
        }

    }

    var listMessages= hashMapOf<Product,Message>() //danh sách tin nhắn của user hiện tại

    fun clean(){
        listMessages.clear() //xoá hết mọi tin nhắn (cái này dùng khi đăng xuất)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun messageFlow(userId: String): Flow<Pair<Product,Message>> = flow{
        AppController.db.collectionGroup("messages").where(
            Filter.or(
                Filter.equalTo("receive", userId), Filter.equalTo("send", userId) //or query
            )
        ).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val productId = document.get("productId").toString()

                    AppController.bindProductById(productId) { product ->
                        runBlocking {
                            val message = fetchMessageForProduct(product)
                            if (message != null) {
                                emit(Pair(product,message)) //phát ra tin nhắn
                            }
                        }

                    }
                }
            }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchAllMessages() { //tìm tất cả tin nhắn cho user hiện tại
        val userId = Firebase.auth.currentUser?.uid ?: return

        runBlocking {
            launch {
                messageFlow(userId).collect(){
                    value->getSingleton().listMessages[value.first]=value.second //lấy giá trị được emit
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchMessageForProduct(product: Product): Message?{ //lấy tin nhắn liên quan tới một sản phẩm nhất định
        var message: Message?=null
        val userId = Firebase.auth.currentUser?.uid

        //lấy những tin nhắn của mình liên quan tới sản phẩm
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

                    //đặt content cho tin nhắn
                    for (content in contents){
                        val text = content["text"].toString()
                        val sender = content["sender"].toString()
                        val recipient = content["recipient"].toString()
                        val time = content["time"].toString() //convert to datetime

                        messageContents.add(
                            MessageContent(text,sender,recipient,
                                LocalDateTime.parse(time, AppController.formatter))
                        )
                    }

                    AppController.bindProductById(productId) { bindedProduct ->
                        message = Message(messageContents, a, b, bindedProduct)

                    }

                }
            }

        return message
    }
}

class MessageRepository{
    //cập nhật tin nhắn realtime
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateMessages(){
        withContext(Dispatchers.IO) {
            val userId = Firebase.auth.currentUser?.uid

            AppController.db.collectionGroup("messages").where(
                Filter.or(
                    Filter.equalTo("receive", userId), Filter.equalTo("send", userId) //or query
                )
            ).get()
                .addOnSuccessListener { documents ->
                    for (document in documents!!.documentChanges) { //trong những document có sự thay đổi
                        if (document.type == DocumentChange.Type.ADDED) //khi có sự cập nhật trong đoạn chat (có tin nhắn mới)
                        {
                            val productId = document.document.get("productId").toString()
                            AppController.bindProductById(productId) { product ->
                                runBlocking {
                                    val singletonMessageController =
                                        MessageController.getSingleton()
                                    val message =
                                        singletonMessageController.fetchMessageForProduct(product)

                                    if (message != null) {
                                        singletonMessageController.listMessages[product] =
                                            message //cập nhật tin nhắn mới
                                    }

                                }

                            }
                        }


                    }
                }
        }

    }
}