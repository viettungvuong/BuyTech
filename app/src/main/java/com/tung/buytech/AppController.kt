package com.tung.buytech

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.LinkedList

class AppController {
    companion object {
        @JvmField
        val productList: LinkedList<Product> = LinkedList()
        var userId: String = String() //chứa userID
        val db = Firebase.firestore
        val storageRef = Firebase.storage.reference

        @JvmStatic fun getDb(): FirebaseFirestore {
            return this.db
        }
    }
    class Product(name: String, price: Int, imageFile: String ){
        public var name: String = name
        public var price: Int = price
        public var imageFile: String = imageFile
        //khúc này là constructor của class

    }



}