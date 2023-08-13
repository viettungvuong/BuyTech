package com.tung.buytech.control

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tung.buytech.control.AppController.Companion.cart
import com.tung.buytech.control.AppController.Companion.db
import com.tung.buytech.objects.Cart
import com.tung.buytech.objects.Order

class CartController {
    companion object{
        @JvmField
        var numberOfCarts=0
        var carts = ArrayList<Cart>()

        @JvmStatic
        fun retrieveAllCarts(){
            db.collection(collectionUsers).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                document -> numberOfCarts=(document.getLong("number-of-carts")?:0L).toInt()
            }

            val getCart = db.collection(collectionUsers).document(Firebase.auth.currentUser!!.uid)
                .collection("carts")

            getCart.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.id=="0"){
                            continue //bỏ qua thằng 0
                        }
                        val currentCart=Cart((document.getString("content"))?:"")
                        carts.add(currentCart!!) //thêm vào danh sách các cart
                    }

                    cart=carts[numberOfCarts] //resume cart
                }
        }
    }
}

class OrderController {
    companion object{
        @JvmField
        var numberOfOrders=0
        var orders = ArrayList<Order>()

        @JvmStatic
        fun retrieveAllCarts(){
            db.collection(collectionUsers).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                    document -> numberOfOrders=(document.getLong("number-of-orders")?:0L).toInt()
            }

            val getCart = db.collection(collectionUsers).document(Firebase.auth.currentUser!!.uid)
                .collection("orders")

            getCart.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.id=="0"){
                            continue //bỏ qua thằng 0
                        }
                        val cartNo = (document.getLong("cart")?:0).toInt()
                        orders.add(Order(CartController.carts[cartNo])) //thêm order mới
                    }

                }
        }
    }

}