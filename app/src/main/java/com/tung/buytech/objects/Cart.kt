package com.tung.buytech.objects

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.control.AppController.Companion.bindProductById
import com.tung.buytech.control.AppController.Companion.db
import com.tung.buytech.control.CartController
import com.tung.buytech.control.collectionUsers
import java.util.LinkedList

class Cart(): java.io.Serializable {
    var cartList = LinkedList<Product>()

    constructor(str: String) : this() {
        val splitList = str.split(',')

        for (productId in splitList) {
            bindProductById(productId) { product ->
                if (product != null) {
                    cartList.add(product)
                }
            }
        }
    }

    override fun toString(): String {
        var str = ""

        for (product in cartList){
            str+=product.productId+","
        }

        str.dropLast(1) //xoá kí tự cuối cùng

        return str
    }

    fun updateToFirebase(){
        val dataMap = hashMapOf(
            "content" to this.toString()
        )
        db.collection(collectionUsers).document(Firebase.auth.currentUser!!.uid).collection("carts")
            .document(CartController.numberOfCarts.toString()).set(dataMap)
    }
}