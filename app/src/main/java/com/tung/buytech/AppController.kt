package com.tung.buytech

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.tung.buytech.MainActivity.Companion.collectionProducts
import java.util.LinkedList

class AppController {
    companion object {
        @JvmField
        val productList: LinkedList<Product> = LinkedList()
        var userId: String = String() //chứa userID
        var db = Firebase.firestore
        val storageRef = Firebase.storage.reference

        @JvmStatic fun getDatabaseInstance(): FirebaseFirestore {
            return this.db
        }
    }
    open class Product(name: String, price: Int, imageFile: String, productId: String ){
        public var name: String = name
        public var price: Int = price
        public var imageFile: String = imageFile
        public var productId: String= productId
        //khúc này là constructor của class

    }

    //inheritance
    class Favorite(name: String, price: Int, imageFile: String, productId: String): Product(name,price,imageFile,productId){
        //ktra thong tin mat hang
        fun updateStatus(){
            val document = db.collection(collectionProducts).document(productId)
        }
    }

}