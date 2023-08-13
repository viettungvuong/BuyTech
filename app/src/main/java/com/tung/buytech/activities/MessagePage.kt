package com.tung.buytech.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.ktx.Firebase
import com.tung.buytech.R
import com.tung.buytech.control.AppController
import com.tung.buytech.control.AppController.Companion.bindProductById
import com.tung.buytech.control.AppController.Companion.formatter
import com.tung.buytech.control.collectionProducts
import com.tung.buytech.objects.Message
import com.tung.buytech.objects.MessageContent
import com.tung.buytech.objects.MessageController
import com.tung.buytech.objects.Product
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class MessagePage : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
    }
}