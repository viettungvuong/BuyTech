package com.tung.buytech.objects

import com.tung.buytech.control.AppController
import com.tung.buytech.control.collectionProducts

open class Product(var name: String, var price: Long, var imageUrl: String, var productId: String) :
    java.io.Serializable {


    var sold = false //đã bán hay chưa

    //observer design pattern
    fun updateSoldStatus(callback: () -> Unit) {
        val docRef = AppController.db.collection(collectionProducts).document(productId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val inStock = Integer.parseInt(document.getString("stock"))

                        if (inStock > 0) {
                            this.sold = true //đã bán hết sản phẩm
                        }
                    }
                }

            }
    }

}

class Favorite(name: String, price: Long, imageFile: String, productId: String) :
    Product(name, price, imageFile, productId) {
    constructor(product: Product) : this(
        product.name,
        product.price,
        product.imageUrl,
        product.productId
    ) {
        //constructor thứ hai của class
    }

    fun notifyMe() {
        //thông báo khi hết hàng
        updateSoldStatus() {
            if (sold) {
                var notification = "Mặt hàng đã hết :("
                //nếu đã hết hàng thì thông báo
            }

        } //dùng hàm callback để khi nào xong rồi mới báo

    }
}