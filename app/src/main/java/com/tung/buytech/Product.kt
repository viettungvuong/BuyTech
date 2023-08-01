package com.tung.buytech

open class Product(name: String, price: Long, imageUrl: String, productId: String) :
    java.io.Serializable {
    public var name: String = name
    public var price: Long = price
    public var imageUrl: String = imageUrl
    public var productId: String = productId
    //khúc này là constructor của class
    //observer design pattern

    public var sold = false //đã bán hay chưa

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